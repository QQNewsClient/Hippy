//
//  HippyWormholeBusinessHandler.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "HippyWormholeProtocol.h"
#import "HippyBridgeDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@class HippyBridge;
@class HippyRootView;
@class HippyWormholeWrapperView;
@class HippyWormholeViewModel;

@interface HippyWormholeBusinessHandler : NSObject<HippyWormholeDataSource, HippyWormholeDelegate, HippyBridgeDelegate>

@property (nonatomic, strong, readonly) HippyBridge *bridge;
@property (nonatomic, strong, readonly) HippyRootView *rootView;

- (void)configureJSBundle:(NSURL *)commonBundlePath
          indexBundlePath:(NSURL *)indexBundlePath
               moduleName:(NSString *)moduleName
                  isDebug:(BOOL)isDebug
           replaceModules:(nullable NSArray<id<HippyBridgeModule>> *)replaceModules;

- (HippyWormholeWrapperView *)wormholeWrapperView:(NSString *)wormholeId;

- (void)addTargetBridge:(HippyBridge *)targetBridge;

- (void)removeTargetBridge:(HippyBridge *)targetBridge;

- (void)clear;

- (void)notifyWormholeEvent:(HippyWormholeEvent)event extraData:(NSDictionary *)extraData;

#pragma mark - For Native Interfaces
/// 根据原始数据生成一个WormholeViewModel
- (HippyWormholeViewModel *)wormholeViewModelWithRawData:(NSDictionary *)rawData;

/// 添加一个WormholeViewModel至数据源
- (void)enqueueWormholeViewModel:(HippyWormholeViewModel *)viewModel;

/// 移除一个WormholeViewModel从数据源
- (void)dequeueWormholeViewModel:(HippyWormholeViewModel *)viewModel;

@end

NS_ASSUME_NONNULL_END
