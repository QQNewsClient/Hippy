//
//  HippyWormholeEngine.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HippyBridgeDelegate.h"
#import "HippyWormholeProtocol.h"
#import "HippyNativeVueManager.h"

//!!IMPORTANT!!! To make sure WormholeSDK works well, pleas install NativeVueSDK first.
//pod 'NativeVueSDK', :git => "http://git.code.oa.com/NativeVue/nativevue-engine.git", :tag => '0.0.6'

NS_ASSUME_NONNULL_BEGIN
@class HippyWormholeBusinessHandler;
@class HippyWormholeViewModel;

@interface HippyWormholeEngine : NSObject

/**
 * load wormhole jsbundle, implement HippyWormholeDataSource / HippyWormholeDelegate.
 */
@property (nonatomic, strong, readonly) HippyWormholeBusinessHandler *businessHandler;

/**
 * check whether  NativeVue  is enabled, default is NO.
 */
@property (nonatomic, assign, readonly) BOOL nativeVueEnabled;

/**
 * check whether  NativeVue debug mode  is enabled, default is NO.
*/
@property (nonatomic, assign, readonly) BOOL nativeVueDebugEnabled;

/**
 * singleton of HippyWormholeEngine.
*/
+ (instancetype)sharedInstance;

/**
 * launch Wormhole Engine.
 * @commonBundlePath: hippy commonBundlePath.
 * @indexBundlePath: hippy indexBundlePath.
 * @moduleName: name of module.
 * @replaceModules:
 * @isDebug: whether debug mode is enabled.
*/
- (void)launchEngine:(NSURL *)commonBundlePath
     indexBundlePath:(NSURL *)indexBundlePath
          moduleName:(NSString *)moduleName
      replaceModules:(nullable NSArray<id<HippyBridgeModule>> *)replaceModules
             isDebug:(BOOL)isDebug;

/**
 * launch Wormhole Engine.
 * @wormholeBusinessHandler: business custom wormholeBusinessHandler.
 */
- (void)launchWithWormholeBusinessHandler:(HippyWormholeBusinessHandler *)wormholeBusinessHandler;

/**
 * load native vue file
 * @data: native vue dom data
 */
- (BOOL)loadNativeVueDomData:(NSData *)data;

/**
 * shutdown Wormhole Engine.
 */
- (void)shutdownEngine;

/**
 * bind Target Bridge to Wormhole Bridge, make it possible for bridges' communication(eg. Data Transmission, Event Communication).
 * @enableDelegate: whether HippyWormholeBusinessHandler is enabled to implement HippyWormholeDelegate.
 * @enableDataSource: whether HippyWormholeBusinessHandler is enabled to implement HippyWormholeDataSource.
 */
- (void)bindTargetBridge:(HippyBridge *)targetBridge
  enableWormholeDelegate:(BOOL)enableDelegate
enableWormholeDataSource:(BOOL)enableDataSource;

/**
 * cancel bind between Target Bridge and Wormhole Bridge.
 */
- (void)cancelBindTargetBridge:(HippyBridge *)targetBridge;

/**
* dispatch Event to Wormhole.
* @eventName: event name.
* @data: event data.
*/
- (void)dispatchWormholeEvent:(NSString *)eventName data:(NSDictionary *)data;

/**
 * open / close FPS Monitor, for debug mode only.
*/
- (void)switchFPSMonitor:(BOOL)on;

/**
 * open / close NativeVue debug, for debug mode only.
*/
- (void)switchNativeVueDebug:(BOOL)on;

#pragma mark - For Native Case
/**
 * create Wormhole Viewmodel with raw data. This method should not be called on main thread!
 */
- (HippyWormholeViewModel *)newWormholeViewModel:(NSDictionary *)data;

@end

NS_ASSUME_NONNULL_END
