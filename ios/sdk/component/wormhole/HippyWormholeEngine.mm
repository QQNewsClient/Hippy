//
//  HippyWormholeEngine.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeEngine.h"
#import "HippyWormholeBusinessHandler.h"
#import "HippyEventDispatcher.h"
#import "HippyBridge+private.h"
#import "HippyWormholeFPSLabel.h"
#import "HippyWormholeThreadUtil.h"
#import "HippyWormholeViewModel.h"
#import "HippyAssert.h"

@interface HippyWormholeEngine()
{
    BOOL _hasStarted;
}
@property (nonatomic, strong, readwrite) HippyWormholeBusinessHandler *businessHandler;
@property (nonatomic, assign, readwrite) BOOL nativeVueEnabled;
@property (nonatomic, assign, readwrite) BOOL nativeVueDebugEnabled;
@property (nonatomic, strong, readwrite) HippyWormholeFPSLabel *fpsLabel;

@end

@implementation HippyWormholeEngine{

}

#pragma mark - Life Cycle
+ (instancetype)sharedInstance
{
    static HippyWormholeEngine *sharedInstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sharedInstance = [[HippyWormholeEngine alloc] init];
    });
    return sharedInstance;
}

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        
    }
    
    return self;
}

#pragma mark - Public Methods
- (void)launchEngine:(NSURL *)commonBundlePath
     indexBundlePath:(NSURL *)indexBundlePath
          moduleName:(NSString *)moduleName
      replaceModules:(nullable NSArray<id<HippyBridgeModule>> *)replaceModules
             isDebug:(BOOL)isDebug
{
    if (_hasStarted)
    {
        return;
    }
    [self.businessHandler configureJSBundle:commonBundlePath indexBundlePath:indexBundlePath moduleName:moduleName isDebug:isDebug replaceModules:replaceModules];
    _hasStarted = YES;
    [self setNativeVueHandler:[NSClassFromString(@"IOSNativeVueEngine") shareInstance]];
}

- (void)launchWithWormholeBusinessHandler:(HippyWormholeBusinessHandler *)wormholeBusinessHandler {
    if (_hasStarted) {
        return;
    }
    _businessHandler = wormholeBusinessHandler;
    _hasStarted = YES;
    [self setNativeVueHandler:[NSClassFromString(@"IOSNativeVueEngine") shareInstance]];
}

- (BOOL)loadNativeVueDomData:(NSData *)data {
    if (!data) {
        return NO;
    }
    return [[HippyNativeVueManager shareInstance] loadResource:data];
}

- (void)setNativeVueHandler:(id<NativeVueProtocol>)handler{
    if (handler) {
        self.nativeVueEnabled = YES;
        [HippyNativeVueManager shareInstance].handler = handler;
        [self _registerDeviceInfoToNativeVueContext];
    }
}

- (void)shutdownEngine
{
    [self.businessHandler clear];
    self.businessHandler = nil;

    _hasStarted = NO;
    self.nativeVueEnabled = NO;
}

- (void)bindTargetBridge:(HippyBridge *)targetBridge
  enableWormholeDelegate:(BOOL)enableDelegate
enableWormholeDataSource:(BOOL)enableDataSource
{
    [self.businessHandler addTargetBridge:targetBridge];
    if (enableDelegate)
    {
        targetBridge.wormholeDelegate = self.businessHandler;
    }
    
    if (enableDataSource)
    {
        targetBridge.wormholeDataSource = self.businessHandler;
    }
}

- (void)cancelBindTargetBridge:(HippyBridge *)targetBridge
{
    targetBridge.wormholeDelegate = nil;
    targetBridge.wormholeDataSource = nil;
    [self.businessHandler removeTargetBridge:targetBridge];
}

- (void)dispatchWormholeEvent:(NSString *)eventName data:(NSDictionary *)data
{
    [self.businessHandler.bridge.eventDispatcher dispatchEvent:@"EventDispatcher"
                                                    methodName:@"receiveNativeEvent"
                                                          args:@{@"eventName": eventName, @"extra": data}];
}

- (void)switchFPSMonitor:(BOOL)on
{
#if DEBUG
    if (on)
    {
        [HippyWormholeThreadUtil performOnMainThreadWithBlock:^{
            self.fpsLabel.hidden = NO;
        }];
    }
    else
    {
        [HippyWormholeThreadUtil performOnMainThreadWithBlock:^{
            [self.fpsLabel removeFromSuperview];
            self.fpsLabel = nil;
        }];
    }
#endif
}

- (void)switchNativeVueDebug:(BOOL)on
{
#if DEBUG
    if (on)
    {
        self.nativeVueDebugEnabled = YES;
    }
    else
    {
        self.nativeVueDebugEnabled = NO;
    }
#endif
}

#pragma mark - For Native Case
- (HippyWormholeViewModel *)newWormholeViewModel:(NSDictionary *)data
{
    HippyAssert(![NSThread isMainThread], @"Wormhole ViewModel should not be created on Main Thread!");
    
    if (!data || ![data isKindOfClass:[NSDictionary class]])
    {
        return nil;
    }
    
    HippyWormholeViewModel *viewModel = [self.businessHandler wormholeViewModelWithRawData:data];
    [self.businessHandler enqueueWormholeViewModel:viewModel];
    
    return viewModel;
}

#pragma mark - Lazy Load
- (HippyWormholeBusinessHandler *)businessHandler
{
    if (!_businessHandler)
    {
        _businessHandler = [[HippyWormholeBusinessHandler alloc] init];
    }
    
    return _businessHandler;
}

- (HippyWormholeFPSLabel *)fpsLabel
{
    if (!_fpsLabel)
    {
        CGFloat width = 55.0f;
        CGFloat height = 20.0f;
        CGFloat x = CGRectGetWidth([UIScreen mainScreen].bounds) - width;
        CGFloat y = 64.0f;
        _fpsLabel = [[HippyWormholeFPSLabel alloc] initWithFrame:CGRectMake(x, y, width, height)];
        UIWindow *window = [[[UIApplication sharedApplication] delegate] window];
        if (window)
        {
            [window addSubview:_fpsLabel];
        }
    }
    
    return _fpsLabel;
}

#pragma mark - initNativeEngine{

- (void)_registerDeviceInfoToNativeVueContext{
    NSDictionary * deviceInfo = [self.businessHandler.bridge.batchedBridge deviceInfo];
    [deviceInfo enumerateKeysAndObjectsUsingBlock:^(id  _Nonnull key, id  _Nonnull obj, BOOL * _Nonnull stop) {
        [[HippyNativeVueManager shareInstance] registerGlobalVaribleWithKey:key value:obj];
    }];
}

@end
