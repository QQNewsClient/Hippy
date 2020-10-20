//
//  HippyNVComponentTreeBuilder.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyNVComponentTreeBuilder.h"
#import "HippyUIManager+NativeVue.h"
#import "HippyBaseListViewProtocol.h"
#import "HippyNVComponent.h"
#import "HippyRootShadowView.h"
#import "HippyVirtualNode.h"

#define kChildrenKey @"children"

@implementation HippyNVComponentTreeBuilder {
   __weak HippyUIManager *_contenxt;
   NSMutableSet<id<HippyComponent>> *_bridgeTransactionListeners;
   NSMutableDictionary<NSNumber *, UIView *> *_viewRegistry; // Main thread only
   NSMutableDictionary<NSNumber *, HippyVirtualNode *> *_nodeRegistry;
   NSMutableDictionary<NSNumber *, HippyShadowView *> *_shadowViewRegistry;
   NSMutableDictionary<NSString *, HippyComponentData *> *_componentDataByName;
   NSMutableArray<NSNumber *> * _listTags;
   NSMutableArray * _uiBlocks;
   NSMutableArray * _virtualNodeBlocks;
   HippyNVComponent * _rootComponent;
}

- (instancetype)initWithContext:(HippyUIManager *)context {
    if (self = [super init]) {
        _contenxt = context;
        _viewRegistry = [NSMutableDictionary new];
        _nodeRegistry = [NSMutableDictionary new];
        _shadowViewRegistry = [NSMutableDictionary new];
        _componentDataByName = [NSMutableDictionary new];
        _listTags = [NSMutableArray new];
        _uiBlocks = [NSMutableArray new];
        _virtualNodeBlocks = [NSMutableArray new];
    }
    return self;
}


#pragma mark - public

- (HippyNVComponent *)buildComponentTreeWithVirtualDom:(NSDictionary *)virtualDom{
    _rootComponent = nil;
    [self _addComponent:virtualDom toSupercomponent:nil atIndex:0];
    [self _layout];
    return _rootComponent;
}

- (UIView * _Nullable)view{
    [self _mountIfNeed];
    HippyAssert(_rootComponent, @"can't be nil");
    return _rootComponent.view;
}

- (HippyComponentData * _Nullable)componentDataWithViewName:(NSString *)viewName {
    if(!viewName){
        return nil;
    }
    HippyComponentData * componentData = _componentDataByName[viewName];
    if (!componentData) {
        componentData = [[_contenxt componentDataWithViewName:viewName] copy];
        _componentDataByName[viewName] = componentData;
    }
    return componentData;
}

- (NSNumber *_Nullable)rootTag {
    return _rootComponent.tag;
}

- (void)addUIBlock:(HippyViewManagerUIBlock)block {
    if (block) {
        [_uiBlocks addObject:block];
    }
}

- (void)addVirtualNodeBlock:(HippyVirtualNodeManagerUIBlock)block {
    if (block) {
        [_virtualNodeBlocks addObject:block];
    }
}

- (HippyUIManager *)uiManager{
   return _contenxt;
}

- (void)addBridgeTransactionListeners:(id<HippyComponent>)listner {
    if (!_bridgeTransactionListeners) {
        _bridgeTransactionListeners = [NSMutableSet new];
    }
    [_bridgeTransactionListeners addObject:listner];
}

- (void)registerViewWithTag:(NSNumber *)tag view:(UIView *)view component:(HippyNVComponent *)component {
    HippyAssert([NSThread isMainThread], @"run on main thread");
    _viewRegistry[tag] = view;
    if ([view conformsToProtocol: @protocol(HippyBaseListViewProtocol)]) {
        id <HippyBaseListViewProtocol> listview = (id<HippyBaseListViewProtocol>)view;
        listview.node = (HippyVirtualList *)component.virtualNode;
        [_listTags addObject:tag];
    }
}


#pragma mark - private

- (void)_addComponent:(NSDictionary *)componentData toSupercomponent:(HippyNVComponent *)parentComponent
              atIndex:(NSInteger)index{
    HippyNVComponent *component = [[HippyNVComponent alloc] initWithJsonData:componentData builder:self];
    if (!_rootComponent) {
        _rootComponent = component;
    }
    _nodeRegistry[component.tag] = component.virtualNode;
    _shadowViewRegistry[component.tag] = component.shadowView;
    [parentComponent insertSubcomponent:component atIndex:index];
    NSArray *children = [componentData objectForKey:kChildrenKey];
    for (NSInteger index = 0; index < children.count; index++) {
         [self _addComponent:children[index] toSupercomponent:component atIndex:index];
    }
}

- (void)flushUIBlocksIfNeed{
    if (_uiBlocks.count == 0) {
        return ;
    }
    HippyAssert([NSThread isMainThread], @"run on main thread");
    for (HippyViewManagerUIBlock block in _uiBlocks) {
        block(_contenxt, _viewRegistry);
    }
    [self flushListView];
    _uiBlocks = [NSMutableArray new];
}

- (void)flushVirtualNodeBlocksIfNeed{
    HippyAssert([NSThread isMainThread], @"run on main thread");
    if (_virtualNodeBlocks.count == 0) {
        return ;
    }
    for (HippyVirtualNodeManagerUIBlock block in _virtualNodeBlocks) {
        block(_contenxt, _nodeRegistry);
    }
    _virtualNodeBlocks = [NSMutableArray new];
}


- (void)flushListView
{
    if (_listTags.count != 0) {
        [_listTags enumerateObjectsUsingBlock:^(NSNumber * _Nonnull tag, __unused NSUInteger idx, __unused BOOL * stop) {
            HippyVirtualList *listNode = (HippyVirtualList *)self->_nodeRegistry[tag];
           if (listNode.needFlush) {
                id <HippyBaseListViewProtocol> listView = (id <HippyBaseListViewProtocol>)self->_viewRegistry[tag];
                if([listView flush]) {
                    listNode.needFlush = NO;
                }
            }
        }];
    }
}

- (void)_layout{
    __weak HippyNVComponentTreeBuilder * weakSelf = self;
    NSDictionary * componentDataByName = [_contenxt componentDataByName];
    for (HippyComponentData *componentData in componentDataByName.allValues) {
        HippyViewManagerUIBlock uiBlock = [componentData uiBlockToAmendWithShadowViewRegistry:_shadowViewRegistry];
        [self addUIBlock:uiBlock];
    }
    HippyRootShadowView * rootView = (HippyRootShadowView *)_rootComponent.shadowView;
    HippyAssert([rootView isKindOfClass:[HippyRootShadowView class]],@"type is error");
    HippyViewManagerUIBlock uiBlock =  [weakSelf uiBlockWithLayoutUpdateForRootView:rootView];
    [self addUIBlock:uiBlock];
    [self _amendPendingUIBlocksWithStylePropagationUpdateForShadowView:rootView];
       
    [self addUIBlock:^(HippyUIManager *uiManager, NSDictionary<NSNumber *,__kindof UIView *> *viewRegistry) {
        if (!weakSelf) {
            return ;
        }
        __strong HippyNVComponentTreeBuilder * strongSelf = weakSelf;
        for (id<HippyComponent> node in strongSelf->_bridgeTransactionListeners) {
               [node hippyBridgeDidFinishTransaction];
        }
    }];
}

- (void)_mountIfNeed{
    [self flushVirtualNodeBlocksIfNeed];
    [self flushUIBlocksIfNeed];
}

- (HippyViewManagerUIBlock)uiBlockWithLayoutUpdateForRootView:(HippyRootShadowView *)rootShadowView
{
    NSSet<HippyShadowView *> *viewsWithNewFrames = [rootShadowView collectViewsWithUpdatedFrames];

    __weak HippyNVComponentTreeBuilder * weakSelf = self;
    if (!viewsWithNewFrames.count) {
        // no frame change results in no UI update block
        return nil;
    }
    typedef struct {
        CGRect frame;
        BOOL isNew;
        BOOL parentIsNew;
        BOOL isHidden;
        BOOL animated;
    } HippyFrameData;

    // Construct arrays then hand off to main thread
    NSUInteger count = viewsWithNewFrames.count;
    NSMutableArray *reactTags = [[NSMutableArray alloc] initWithCapacity:count];
    NSMutableData *framesData = [[NSMutableData alloc] initWithLength:sizeof(HippyFrameData) * count];
    {
        NSUInteger index = 0;
        HippyFrameData *frameDataArray = (HippyFrameData *)framesData.mutableBytes;
        for (HippyShadowView *shadowView in viewsWithNewFrames) {
            reactTags[index] = shadowView.hippyTag;
            frameDataArray[index++] = (HippyFrameData){
                shadowView.frame,
                shadowView.isNewView,
                shadowView.superview.isNewView,
                shadowView.isHidden,
                shadowView.animated
            };
        }
    }
    // These are blocks to be executed on each view, immediately after
    // hippySetFrame: has been called. Note that if hippySetFrame: is not called,
    // these won't be called either, so this is not a suitable place to update
    // properties that aren't related to layout.
    NSMutableDictionary<NSNumber *, HippyViewManagerUIBlock> *updateBlocks =
    [NSMutableDictionary new];
    for (HippyShadowView *shadowView in viewsWithNewFrames) {
        // We have to do this after we build the parentsAreNew array.
        shadowView.newView = NO;
        NSNumber *reactTag = shadowView.hippyTag;
        HippyViewManager *manager = [_contenxt.componentDataByName[shadowView.viewName] manager];
        HippyViewManagerUIBlock block = [manager uiBlockToAmendWithShadowView:shadowView];
        if (block) {
            updateBlocks[reactTag] = block;
        }
        if (shadowView.onLayout) {
            CGRect frame = shadowView.frame;
            shadowView.onLayout(@{
                                  @"layout": @{
                                          @"x": @(frame.origin.x),
                                          @"y": @(frame.origin.y),
                                          @"width": @(frame.size.width),
                                          @"height": @(frame.size.height),
                                          },
                                  });
        }
    }
    [self addVirtualNodeBlock:^(HippyUIManager *uiManager, NSDictionary<NSNumber *,HippyVirtualNode *> *virtualNodeRegistry) {
        NSInteger index = 0;
        HippyFrameData *frameDataArray = (HippyFrameData *)framesData.mutableBytes;
        for (NSNumber *reactTag in reactTags) {
            HippyVirtualNode *node = virtualNodeRegistry[reactTag];
            if (node) {
                  HippyFrameData frameData = frameDataArray[index];
                  [node hippySetFrame: frameData.frame];
            }
            index++;
        }
    }];
    
    // Perform layout (possibly animated)
    return ^(__unused HippyUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
        if(!weakSelf){
            return ;
        }
        __strong HippyNVComponentTreeBuilder * strongSelf = weakSelf;
        const HippyFrameData *frameDataArray = (const HippyFrameData *)framesData.bytes;
        __block NSUInteger completionsCalled = 0;

        NSInteger index = 0;
        for (NSNumber *reactTag in reactTags) {
            HippyFrameData frameData = frameDataArray[index++];

            UIView *view = viewRegistry[reactTag];
            CGRect frame = frameData.frame;

            BOOL isHidden = frameData.isHidden;
            void (^completion)(BOOL) = ^(__unused BOOL finished) {
                completionsCalled++;
            };
            if (view.isHidden != isHidden) {
                view.hidden = isHidden;
            }
            HippyViewManagerUIBlock updateBlock = updateBlocks[reactTag];
            [view hippySetFrame:frame];
//            if (frameData.animated) {//nv not support animated
//                if (nil == view) {
//                    [self->_listAnimatedViewTags addObject:reactTag];
//                }
//                [uiManager.bridge.animationModule connectAnimationToView:view];
//            }
            if (updateBlock) {
                updateBlock(strongSelf->_contenxt, viewRegistry);
            }
            completion(YES);
        }
    };
}

- (void)_amendPendingUIBlocksWithStylePropagationUpdateForShadowView:(HippyShadowView *)topView{
    NSMutableSet<HippyApplierBlock> *applierBlocks = [NSMutableSet setWithCapacity:1];
    NSMutableSet<HippyApplierVirtualBlock> *virtualApplierBlocks = [NSMutableSet setWithCapacity:1];
    [topView collectUpdatedProperties:applierBlocks virtualApplierBlocks: virtualApplierBlocks parentProperties:@{}];
    if (applierBlocks.count) {
        [self addUIBlock:^(__unused HippyUIManager *uiManager, NSDictionary<NSNumber *, UIView *> *viewRegistry) {
            for (HippyApplierBlock block in applierBlocks) {
                block(viewRegistry);
            }
        }];
        [self addVirtualNodeBlock:^(HippyUIManager *uiManager, NSDictionary<NSNumber *,HippyVirtualNode *> *virtualNodeRegistry) {
            for (HippyApplierVirtualBlock block in virtualApplierBlocks) {
                     block(virtualNodeRegistry);
            }
        }];
    }
}

- (void)dealloc{
    
}


@end
