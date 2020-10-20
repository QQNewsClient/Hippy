//
//  HippyWormholeItemManager.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeItemManager.h"
#import "HippyWormholeItem.h"
#import "HippyVirtualWormholeNode.h"

@implementation HippyWormholeItemManager

@synthesize bridge = _bridge;

HIPPY_EXPORT_MODULE(Wormhole)
HIPPY_EXPORT_VIEW_PROPERTY(params, NSDictionary)

- (UIView *)view {
    HippyWormholeItem *view = [[HippyWormholeItem alloc] initWithBridge:_bridge];
    return view;
}

- (HippyVirtualNode *)node:(NSNumber *)tag name:(NSString *)name props:(NSDictionary *)props {
    return [HippyVirtualWormholeNode createNode:tag viewName:name props:props];
}

@end
