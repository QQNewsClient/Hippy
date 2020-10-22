//
//  HippyWormholeViewModel.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeViewModel.h"
#import "HippyBridge.h"
#import "HippyWormholeBaseShadowView.h"
#import "HippyNativeVueViewModel.h"
#import "NSObject+WhormholdViewModel.h"
#import "HippyNativeVueManager.h"
#import "HippyWormholeEngine.h"
#import "HippyWormholeBusinessHandler.h"
#import "HippyNVUtil.h"
#import "HippyWormholeFactory.h"

static NSInteger gWormholeBaseId = 10000;

@interface HippyWormholeViewModel ()<HippyWormholeWrapperViewDelegate>
{
    HippyWormholeWrapperView * _view;
    CGSize _curViewSize;
}

@property (nonatomic, strong) NSDictionary *params;
@property (nonatomic, weak) HippyBridge *bridge;
@property (atomic, strong) HippyNativeVueViewModel *nativeVueModel;

@end

@implementation HippyWormholeViewModel

@synthesize wormholeId = _wormholeId;

- (instancetype)initWithParams:(NSDictionary *)params
{
    self = [super init];
    if (self)
    {
        _wormholeId = [NSString stringWithFormat:@"%ld", (long)gWormholeBaseId++];
        NSMutableDictionary *mutableDict = [NSMutableDictionary  dictionary];
        [mutableDict setObject:_wormholeId forKey:@"wormholeId"];
        if (params)
        {
            [mutableDict addEntriesFromDictionary:params];
        }
        self.params = [NSDictionary dictionaryWithDictionary:mutableDict];
        self.bridge = [HippyWormholeEngine sharedInstance].businessHandler.bridge;
        if ([HippyWormholeEngine sharedInstance].nativeVueEnabled)
        {
            [self _initNativeVueModel];
        }
    }
    return self;
}

- (void)dealloc
{
    UIView * wrapperView = _view;
    dispatch_async(dispatch_get_main_queue(), ^{//保证view主线程释放
        [wrapperView description];
    });
}

- (NSDictionary *)toJSONObject
{
    NSString *wormholeId = self.wormholeId.length > 0 ? self.wormholeId : @"";
    NSInteger index = self.params[@"index"] ? [self.params[@"index"] integerValue] : 0;
    NSDictionary *data = self.params[@"data"] ? self.params[@"data"] : @{};
    
    return @{
        @"wormholeId": wormholeId,
        @"index": @(index),
        @"data": data
    };
}

- (void)wormholeWillAppear
{
    if ([self.delegate respondsToSelector:@selector(wormholeWillAppear:)])
    {
        [self.delegate wormholeWillAppear:self];
    }
}

- (void)wormholeWillDisappear
{
    if ([self.delegate respondsToSelector:@selector(wormholeWillDisappear:)])
    {
        [self.delegate wormholeWillDisappear:self];
    }
}

#pragma mark - public

- (HippyWormholeWrapperView *)view
{
    if (!_view || !_view.contentView) {
        UIView *nvOverlayView = _nativeVueModel.view;
        _view = [self.bridge.wormholeDataSource wormholeViewWithWormholeId:_wormholeId nvOverlayView:nvOverlayView];
        _view.delegate = self;
    }
    return _view;
}


- (CGFloat)viewWidth{
    if (_curViewSize.width) {
        return _curViewSize.width;
    }
    return _nativeVueModel.viewWidth ? : 1;
}

- (CGFloat)viewHeight{
    if (_curViewSize.height) {
         return _curViewSize.height;
    }
    return _nativeVueModel.viewHeight ? : 1;
}

- (void)syncBuild{
    [_nativeVueModel syncBuild];
}

- (void)asyncBuildWithCompletion:(HippyNVBuildCompletion)completion{
     [_nativeVueModel asyncBuildWithCompletion:completion];
}

- (void)setBridge:(HippyBridge *)bridge{
    if (_bridge != bridge) {
        _bridge = bridge;
        [_bridge.wormholeDelegate didCreatedViewModel:self];
        [_bridge.wormholeDelegate didReceiveWormholeData:self.params];
    }
}

- (void)attachViewModelPropsToShadowView:(HippyWormholeBaseShadowView *)shadowView withOriginProps:(NSMutableDictionary *)originProps
{
    if (!shadowView || !originProps)
    {
        return;
    }
    originProps[@"width"] = @(self.viewWidth);
    originProps[@"height"] = @(self.viewHeight);
}

- (void)updateShadowView:(HippyWormholeBaseShadowView *)shadowView onWormholeSizeChanged:(CGSize)size
{
    HippyBridge *bridge = (HippyBridge *)shadowView.owner;
    if (!bridge)
    {
        return;
    }
    NSMutableDictionary * newProps = (NSMutableDictionary *)[shadowView.props mutableCopy];
    newProps[@"width"] = @(size.width);
    newProps[@"height"] = @(size.height);
    [bridge.uiManager executeBlockOnUIManagerQueue:^{
        [bridge.uiManager updateViewWithHippyTag:shadowView.hippyTag props:newProps];
        [bridge.uiManager setNeedsLayout];
    }];
}

#pragma mark - HippyWormholeWrapperViewDelegate

- (void)wrapView:(HippyWormholeWrapperView * _Nullable)wrapView didChangedSize:(CGSize)size{
    
    if (CGSizeEqualToSize(_curViewSize, size))
    {
        return ;
    }
    _curViewSize = size;
    if ([self.delegate respondsToSelector:@selector(wormholeViewModel:didChangedSize:)]) {
        [self.delegate wormholeViewModel:self didChangedSize:size];
    }
}

- (void)wrapView:(HippyWormholeWrapperView *)wrapView didRemoveNativeView:(UIView *)nativeView{
    dispatch_async(dispatch_get_global_queue(0, 0), ^{
        self->_nativeVueModel = nil;
    });
}

- (void)wrapView:(HippyWormholeWrapperView * _Nullable)wrapView updateProps:(NSDictionary *)props {
    if ([self.delegate respondsToSelector:@selector(wormholeModelUpdate:)]) {
        [self.delegate wormholeModelUpdate:self];
    }
}

#pragma mark - init

- (void)_initNativeVueModel{
    NSString * templateId = self.params[@"templateId"] ? : self.params[@"data"][@"templateType"];//fetch template id
    templateId = [HippyNVUtil toNSString:templateId];
    NSString * vueDomString = [[HippyNativeVueManager shareInstance] jsonDomWithTemplateId:templateId];
    NSDictionary * domData = self.params;
    HippyUIManager * uiManager = self.bridge.uiManager;
    if ([vueDomString isKindOfClass:[NSString class]] && vueDomString.length) {
        _nativeVueModel = [[HippyNativeVueViewModel alloc] initWithJsonDom:vueDomString domData:domData context:uiManager];
    }
}

@end
