//
//  HippyVirtualWormholeNode.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyVirtualWormholeNode.h"
#import "HippyBridge.h"
#import "HippyWormholeFactory.h"
#import "objc/runtime.h"

@implementation HippyVirtualWormholeNode{
    BOOL _didCreatedLayouted;
}
@synthesize wormholeId = _wormholeId;

- (instancetype)initWithTag:(NSNumber *)reactTag viewName:(NSString *)viewName props:(NSDictionary *)props
{
    if (self = [super initWithTag:reactTag viewName:viewName props:props]) {
        self.dataType = HippyBaseListViewItemDataTypeWormhole;
        self.wormholeId = props[@"params"][@"wormholeId"];
    }
    return self;
}

- (void)setOwner:(id)owner {
    [super setOwner:owner];
}

- (BOOL)isLazilyLoadType {
    return YES;
}

- (BOOL)createViewLazily {
    return YES;
}

- (void)dispatchDidCreatedWormholeNodeIfNeed{
    HippyAssert([NSThread isMainThread], @"should run on the main thread");

    if (_didCreatedLayouted || !_wormholeId) {
        return ;
    }
    HippyBridge *bridge = (HippyBridge *)self.owner;
    HippyAssert(bridge,@"can't be nil");
    NSString *wormholeId = self.props[@"params"][@"wormholeId"];
    
    if (wormholeId.length == 0)
    {
        return;
    }
    
    [[bridge wormholeFactory] setWormholeNode:self forWormholeId:wormholeId];
    if ([bridge.wormholeDelegate respondsToSelector:@selector(didCreateWormholeNode:userInfo:)])
    {
        [bridge.wormholeDelegate didCreateWormholeNode:self userInfo:@{@"node": self, @"wormholeId": wormholeId}];
    }
    _didCreatedLayouted = true;
}

- (void)setFrame:(CGRect)frame {
    CGRect oldFrame = self.frame;
    BOOL notifySizeChanged = NO;
    if (!CGRectEqualToRect(oldFrame, frame))
    {
        // 虫洞节点有变化，通知feeds节点更新
        notifySizeChanged = YES;
    }
    
    [super setFrame:frame];
    
//    if (notifySizeChanged && _wormholeId)
//    {
//        if (self.owner && [self.owner isKindOfClass:[HippyBridge class]])
//        {
//            HippyBridge *bridge = (HippyBridge *)self.owner;
//            if ([bridge.wormholeDelegate respondsToSelector:@selector(wormholeView:wormholeId:onSizeChanged:)])
//            {
//                 NSString *wormholeId = self.props[@"params"][@"wormholeId"];
//                 [bridge.wormholeDelegate wormholeView:[bridge.uiManager viewForReactTag:self.reactTag] wormholeId:wormholeId onSizeChanged:CGSizeMake(CGRectGetWidth(frame), CGRectGetHeight(frame))];
//            }
//        }
//    }
    dispatch_async(dispatch_get_main_queue(), ^{
        [self dispatchDidCreatedWormholeNodeIfNeed];
    });
}

- (void)setProps:(NSDictionary *)props {
    [super setProps:props];
    
    HippyBridge *bridge = (HippyBridge *)self.owner;
    HippyAssert(bridge, @"can't be nil");
    if ([bridge.wormholeDelegate respondsToSelector:@selector(didUpdatedWormholeNode:userInfo:)]) {
        NSString *wormholeId = self.props[@"params"][@"wormholeId"];
        [bridge.wormholeDelegate didUpdatedWormholeNode:self
                                               userInfo:@{@"node": self, @"wormholeId": wormholeId}];
    }
}

@end

@implementation HippyVirtualWormholeContainerNode

- (BOOL)isLazilyLoadType {
    return YES;
}

- (BOOL)createViewLazily {
    return YES;
}

@end

