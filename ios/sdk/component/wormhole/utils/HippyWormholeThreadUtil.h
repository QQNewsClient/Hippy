//
//  HippyWormholeThreadUtil.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HippyWormholeThreadUtil : NSObject

+ (void)performOnMainThreadWithBlock:(dispatch_block_t)block;
+ (void)performOnMainThreadWithBlock:(dispatch_block_t)block waitUntilDone:(BOOL)wait;

@end

NS_ASSUME_NONNULL_END
