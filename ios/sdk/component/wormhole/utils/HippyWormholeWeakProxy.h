//
//  HippyWormholeWeakProxy.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HippyWormholeWeakProxy : NSProxy

#if DEBUG

@property (nonatomic, weak, readonly) id target;

- (instancetype)initWithTarget:(id)target;

+ (instancetype)proxyWithTarget:(id)target;

#endif

@end

NS_ASSUME_NONNULL_END
