//
//  HippyWormholeThreadUtil.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeThreadUtil.h"

@implementation HippyWormholeThreadUtil

+ (void)performOnMainThreadWithBlock:(dispatch_block_t)block{
    [[self class] performOnMainThreadWithBlock:block waitUntilDone:NO];
}


+ (void)performOnMainThreadWithBlock:(dispatch_block_t)block waitUntilDone:(BOOL)wait{
    if (!block) {
        return ;
    }
    if ([NSThread isMainThread]) {
        block();
    }else {
        if (wait) {
            dispatch_sync(dispatch_get_main_queue(), block);
        }else {
            dispatch_async(dispatch_get_main_queue(), block);
        }
    }
}


@end
