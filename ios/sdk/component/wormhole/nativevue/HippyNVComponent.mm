//
//  HippyNVComponent.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyNVComponent.h"
#import "HippyNVComponentTreeBuilder.h"
#import "HippyNVUtil.h"
#import "HippyAssert.h"
#import "HippyUIManager+NativeVue.h"
#import "HippyBaseListViewProtocol.h"
#import "HippyRootShadowView.h"
#import "NSObject+NativeVue.h"


#define kTagKey @"id"
#define kTypeKey @"name"
#define kAttrKey @"attr"
#define kStyleKey @"style"
#define kPropsKey @"props"

@implementation HippyNVComponent{
    __weak HippyNVComponentTreeBuilder *_builder;
    __weak HippyNVComponent *_parentComponent;
    NSMutableArray *_subComponents;
    NSNumber *_tag;
    NSString *_type;
    NSMutableDictionary *_props;
    HippyShadowView *_shadowView;
    HippyVirtualNode *_virtualNode;
    HippyComponentData * _componentData;
    UIView * _view;
}

- (instancetype)initWithJsonData:(NSDictionary *)jsonData builder:(HippyNVComponentTreeBuilder *)builder {
    if (self = [super init]) {
        _builder = builder;
        _subComponents = [NSMutableArray new];
        _tag = [HippyNVUtil toNSNumber:jsonData[kTagKey]];
        _type = [HippyNVUtil toNSString:jsonData[kTypeKey]];
        _props = [self _getPropsWithJsonData:jsonData];
        [self _createInnerNodeWithTag:_tag viewName:_type props:_props];
    }
    return self;
}

#pragma mark - public

- (void)insertSubcomponent:(HippyNVComponent *)subcomponent atIndex:(NSInteger)index {
    [_subComponents insertObject:subcomponent atIndex:index];
    subcomponent->_parentComponent = self;
    [_virtualNode insertHippySubview:subcomponent.virtualNode atIndex:index];
    [_shadowView insertHippySubview:subcomponent.shadowView atIndex:index];
    
    HippyVirtualNode * node = subcomponent.virtualNode;
    if ([node createViewLazily]) {
        return ;
    }
    [_builder addUIBlock:^(HippyUIManager *uiManager, NSDictionary<NSNumber *,__kindof UIView *> *viewRegistry) {
         [self.view insertSubview:subcomponent.view atIndex:index];
    }];
}

#pragma mark - private


- (void)_createInnerNodeWithTag:(NSNumber *)tag viewName:(NSString *)viewName props:(NSMutableDictionary *)props {
    HippyAssert(tag && viewName, @"tag or viewName can't be nil");
    [self _filterWithViewName:viewName props:props];
    HippyComponentData * componentData = [_builder componentDataWithViewName:viewName];
    _componentData = componentData;
    if (componentData == nil) {
        HippyLogError(@"No component found for view with name \"%@\"", viewName);
    }
    NSNumber * rootTag = [_builder rootTag];
    if (rootTag) {
        [props setValue: rootTag forKey: @"rootTag"];
    }
    if (!rootTag) {//rootTag
        _shadowView = [HippyRootShadowView new];
        ((HippyRootShadowView *)_shadowView).sizeFlexibility = HippyRootViewSizeFlexibilityNone;
    }else {
        _shadowView = [componentData createShadowViewWithTag:tag];
    }
   
    HippyAssert(_shadowView, @"can't be nil");
    _shadowView.hippyTag = tag;
    _shadowView.viewName = viewName;
    _shadowView.props = props;
    _shadowView.rootTag = rootTag;
    [componentData setProps:props forShadowView:_shadowView];
    _virtualNode = [componentData createVirtualNode:tag props: props];
    _virtualNode.rootTag = rootTag;
    _virtualNode.owner = [_builder.uiManager bridge];
    HippyAssert(_virtualNode, @"can't be nil");
}


- (UIView *)_createView{
    HippyAssert([NSThread isMainThread], @"run on main thread");
    UIView *view = [_componentData createViewWithTag:_tag initProps:_props];
    if (view) {
       view.viewName = [self viewName];
       [_componentData setProps:_props forView:view]; // Must be done before bgColor to prevent wrong default
       if ([view respondsToSelector:@selector(hippyBridgeDidFinishTransaction)]) {
           [_builder addBridgeTransactionListeners:view];
       }
       [_builder registerViewWithTag:_tag view:view component:self];
    }
    return view;
}

- (NSMutableDictionary *)_getPropsWithJsonData:(NSDictionary *)jsonData {
    NSDictionary * props = [HippyNVUtil toNSDictionary:jsonData[kPropsKey]];
    if (props.count) {
        return [props mutableCopy];
    }
    NSMutableDictionary * newProps = [NSMutableDictionary new];
    NSDictionary * attr = [HippyNVUtil toNSDictionary:jsonData[kAttrKey]];
    NSDictionary * style = [HippyNVUtil toNSDictionary:jsonData[kStyleKey]];
    [newProps addEntriesFromDictionary:attr];
    [newProps addEntriesFromDictionary:style];
    return newProps;
}


- (void)_filterWithViewName:(NSString *)viewName props:(NSMutableDictionary *)props{
    if ([viewName isEqualToString:@"Image"]) {
        id source = props[@"source"] ? : props[@"src"];
        if ([source isKindOfClass:[NSString class]]) {
            [props setObject:@[@{@"uri":source}] forKey:@"source"];
        }else if([source isKindOfClass:[NSDictionary class]]){
             [props setObject:@[source] forKey:@"source"];
        }
    }
}
#pragma mark - getter

- (NSNumber *)tag {
    return _tag;
}

- (NSString *)viewName {
    return _type;
}

- (HippyShadowView *)shadowView {
    return _shadowView;
}

- (HippyVirtualNode *)virtualNode {
    return _virtualNode;
}

- (HippyNVComponent *)parentComponent {
    return _parentComponent;
}

- (HippyUIManager *)uiManager{
    return _builder.uiManager;
}

- (UIView *)view{
    if (!_view) {
       _view = [self _createView];
       _view.hippy_from_nativevue = true;
    }
    return _view;
}

- (CGRect)frame{
    return _shadowView.frame;
}

- (void)dealloc{
    
}



@end
