//
//  HippyWormholeContainerManager.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeContainerManager.h"
#import "HippyVirtualWormholeNode.h"

@implementation HippyWormholeContainerManager

HIPPY_EXPORT_MODULE(WormholeContainer)

- (HippyVirtualNode *)node:(NSNumber *)tag name:(NSString *)name props:(NSDictionary *)props {
    return [HippyVirtualWormholeContainerNode createNode:tag viewName:name props:props];
}

@end
