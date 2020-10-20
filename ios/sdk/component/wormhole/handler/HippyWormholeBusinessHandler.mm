//
//  HippyWormholeBusinessHandler.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyWormholeBusinessHandler.h"
#import "HippyRootView.h"
#import "HippyBridge.h"
#import "HippyVirtualWormholeNode.h"
#import "HippyWormholeFactory.h"
#import "HippyVirtualNode.h"
#import "HippyWormholeWrapperView.h"
#import "HippyWormholeItem.h"
#import "HippyWormholeViewModel.h"
#import "HippyShadowView.h"
#import "HippyWormholeTagShadowView.h"
#import "HippyNativeVueViewModel.h"
#import "HippyWormholeThreadUtil.h"

@interface HippyWormholeBusinessHandler () <HippyWormholeViewModelDelegate>
{
    NSMutableArray<NSString *> *_wormholeViewModelKeys;
    NSMutableDictionary<NSString *, HippyWormholeViewModel *> *_wormholeViewModelDict;
}

@property (nonatomic, strong, readwrite) HippyBridge *bridge;
@property (nonatomic, strong, readwrite) HippyRootView *rootView;

@property (nonatomic, strong) NSPointerArray *targetBridgeList;

@end

@implementation HippyWormholeBusinessHandler

- (instancetype)init
{
    self = [super init];
    if (self)
    {
        _wormholeViewModelKeys = [NSMutableArray array];
        _wormholeViewModelDict = [NSMutableDictionary dictionary];
        
        self.targetBridgeList = [NSPointerArray weakObjectsPointerArray];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(applicationBecomeActive:)
                                                     name:UIApplicationWillEnterForegroundNotification object:nil];
        
        [[NSNotificationCenter defaultCenter] addObserver:self
                                                 selector:@selector(applicationEnterBackground:)
                                                     name: UIApplicationDidEnterBackgroundNotification object:nil];
    }
    
    return self;
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

#pragma mark - Public Methods
- (void)configureJSBundle:(NSURL *)commonBundlePath
          indexBundlePath:(NSURL *)indexBundlePath
               moduleName:(NSString *)moduleName
                  isDebug:(BOOL)isDebug
           replaceModules:(nullable NSArray<id<HippyBridgeModule>> *)replaceModules
{
    if (!commonBundlePath.absoluteString.length || !indexBundlePath.absoluteString.length)
    {
        return;
    }
    
    if (self.rootView || self.bridge)
    {
        return;
    }
    
    NSArray *tmpReplaceModules = [replaceModules copy];
    
    BOOL isSimulator = NO;
#if TARGET_IPHONE_SIMULATOR
    isSimulator = YES;
#endif
    
    NSDictionary *debugKey = @{@"DebugMode": @(isDebug)};
    if (tmpReplaceModules) {
        self.bridge = [[HippyBridge alloc] initWithBundleURL:commonBundlePath
                                              moduleProvider:^NSArray<id<HippyBridgeModule>> *{
            return tmpReplaceModules;
        }
                                               launchOptions:debugKey
                                                 executorKey:nil];
    }
    else
    {
        self.bridge = [[HippyBridge alloc] initWithDelegate:self bundleURL:commonBundlePath moduleProvider:nil launchOptions:debugKey executorKey:nil];
    }
    
    self.bridge.wormholeDataSource = self;
    self.bridge.wormholeDelegate = self;
    
    if (isDebug)
    {
        self.rootView = [[HippyRootView alloc] initWithBridge:self.bridge
                                                 moduleName:moduleName
                                          initialProperties:nil
                                               shareOptions:nil
                                                   delegate:nil];
    }
    else
    {
        self.rootView = [[HippyRootView alloc] initWithBridge: self.bridge
                                                businessURL: indexBundlePath
                                                 moduleName: moduleName
                                          initialProperties: @{@"isSimulator": @(isSimulator)}
                                              launchOptions: nil
                                               shareOptions: nil
                                                  debugMode: NO
                                                   delegate: nil];
    }
}

- (HippyWormholeWrapperView *)wormholeWrapperView:(NSString *)wormholeId;
{
    if (wormholeId.length == 0)
    {
        return nil;
    }
    
    HippyWormholeWrapperView *wrapperView = [self.bridge.wormholeFactory wormholeWrapperViewForWormholeId:wormholeId];
    return wrapperView;
}

- (void)clear
{
    [_wormholeViewModelKeys removeAllObjects];
    [_wormholeViewModelDict removeAllObjects];
    
    _wormholeViewModelKeys = nil;
    _wormholeViewModelDict = nil;
    
    [self.bridge.wormholeFactory clear];
    self.bridge.wormholeFactory = nil;
    
    self.bridge = nil;
    self.rootView = nil;
}

- (void)addTargetBridge:(HippyBridge *)targetBridge
{
    if (!targetBridge)
    {
        return;
    }
    
    for (HippyBridge *bridge in self.targetBridgeList)
    {
        if (bridge == targetBridge)
        {
            return;
        }
    }
    
    [self.targetBridgeList addPointer:(__bridge void* _Nullable)targetBridge];
}

- (void)removeTargetBridge:(HippyBridge *)targetBridge
{
    if (!targetBridge)
    {
        return;
    }
    
    for (int i = 0; i < self.targetBridgeList.count; ++i)
    {
        HippyBridge *bridge = (HippyBridge *)[self.targetBridgeList pointerAtIndex:i];
        if (bridge == targetBridge)
        {
            [self.targetBridgeList removePointerAtIndex:i];
            break;
        }
    }
}

- (void)notifyWormholeEvent:(HippyWormholeEvent)event extraData:(NSDictionary *)extraData
{
    if (!extraData || ![extraData isKindOfClass:[NSDictionary class]])
    {
        return;
    }
    
    NSString *wormholeId = nil;
    NSInteger index = -1;
    if (extraData[@"wormholeId"] && [extraData[@"wormholeId"] isKindOfClass:[NSString class]])
    {
        wormholeId = extraData[@"wormholeId"];
    }
    
    if (extraData[@"index"] && [extraData[@"index"] isKindOfClass:[NSNumber class]])
    {
        index = [extraData[@"index"] integerValue];
    }
    
    NSString *eventName = nil;
    switch (event) {
        case HippyWormholeEventViewDidAppear:
            eventName = @"Wormhole.didAppear";
            break;
        case HippyWormholeEventViewDidDisappear:
            eventName = @"Wormhole.didDisappear";
            break;
        case HippyWormholeEventEnterForeground:
            eventName = @"Wormhole.enterForeground";
            break;
        case HippyWormholeEventEnterBackground:
            eventName = @"Wormhole.enterBackground";
            break;
        default:
            break;
    }
    
    if (eventName)
    {
        NSLog(@"ciro debug:[wormholeView didTrigEvent]:%@, wormholeId:%@, index:%ld", eventName, wormholeId, index);
        
        [self.rootView.bridge.eventDispatcher dispatchEvent:@"EventDispatcher"
                                                 methodName:@"receiveNativeEvent"
                                                       args:@{@"eventName": eventName, @"extra": extraData}];
    }
}

#pragma mark - Private Methods

#pragma mark - HippyBridgeDelegate
- (void)didReceiveMessage:(NSDictionary *)message forBridge:(HippyBridge *)bridge {
    NSLog(@"wormhole bridge sends message to target birdge. message:%@", message);
    
    for (HippyBridge *targetBridge in self.targetBridgeList)
    {
        [targetBridge.eventDispatcher dispatchEvent:@"EventDispatcher"
                                         methodName:@"receiveNativeEvent"
                                               args:@{@"eventName": @"onClientMessageReceived", @"extra": message}];
    }
}

#pragma mark - HippyWormholeDataSource
- (HippyWormholeWrapperView *)wormholeViewWithWormholeId:(NSString *)wormholeId
                                         nvOverlayView:(UIView * _Nullable)nvOverlayView
{
    HippyAssert([NSThread isMainThread],@"should run on the main thread");
    HippyWormholeWrapperView * wormholdView = [self.bridge.wormholeFactory wormholeWrapperViewForWormholeId:wormholeId];
    HippyVirtualWormholeNode * node = [self.bridge.wormholeFactory wormholeNodeForWormholeId:wormholeId];
    if (!wormholdView) {
        wormholdView = [[HippyWormholeWrapperView alloc] initWithWormholeId:wormholeId natvieView:nvOverlayView];
        [self.bridge.wormholeFactory setWormholeWrapperView:wormholdView forWormholeId:wormholeId];
    }
    
    if (node) {
        [self setContentViewWithWormholeId:wormholeId node:node wrapView:wormholdView];
    }
    
    return wormholdView;
}

#pragma mark - HippyWormholeDelegate

- (void)didReceiveWormholeData:(NSDictionary *)data{
    [self.bridge.eventDispatcher dispatchEvent:@"EventDispatcher"
                                    methodName:@"receiveNativeEvent"
                                          args:@{@"eventName": @"Wormhole.dataReceived", @"extra": @[data]}];
}

- (void)didCreatedViewModel:(HippyWormholeViewModel *)wholeViewModel{
    
    [HippyWormholeThreadUtil performOnMainThreadWithBlock:^{
        [self.bridge.wormholeFactory setWormholeViewModel:wholeViewModel forWormholeId:wholeViewModel.wormholeId];
    }];
}

- (void)didCreateWormholeNode:(HippyVirtualWormholeNode *)wormholeNode userInfo:(NSDictionary *)userInfo
{
    if (!userInfo || !wormholeNode)
    {
        return;
    }
    
    NSString *wormholeId = userInfo[@"wormholeId"];
    
    if (wormholeId.length == 0)
    {
        return;
    }
    
    HippyWormholeFactory *wormholeFactory = [self.bridge wormholeFactory];
    [wormholeFactory setWormholeNode:wormholeNode forWormholeId:wormholeId];
    
    // create WrapperView first
    HippyWormholeWrapperView *wrapperView = [wormholeFactory wormholeWrapperViewForWormholeId:wormholeId];
    if (!wrapperView) {
        return ;
    }
    [self setContentViewWithWormholeId:wormholeId node:wormholeNode wrapView:wrapperView];
}

- (void)didUpdatedWormholeNode:(HippyVirtualWormholeNode *)wormholeNode userInfo:(NSDictionary *)userInfo {
    if (!userInfo || !wormholeNode) {
        return;
    }
    
    NSString *wormholeId = userInfo[@"wormholeId"];
    
    if (wormholeId.length == 0) {
        return;
    }
    
    HippyWormholeFactory *wormholeFactory = [self.bridge wormholeFactory];
    
    // create WrapperView first
    HippyWormholeWrapperView *wrapperView = [wormholeFactory wormholeWrapperViewForWormholeId:wormholeId];
    if (!wrapperView) {
        return ;
    }
    
    [wrapperView updateContentViewWithUserInfo:userInfo];
}

#pragma mark -
- (void)setContentViewWithWormholeId:(NSString *)wormholeId
                                node:(HippyVirtualWormholeNode *)node
                            wrapView:(HippyWormholeWrapperView *)wrapperView
{
    UIView *wormholeView = [self.bridge.uiManager createViewFromNode:node];
    wrapperView.wormholeNode = node;
    
    if (wormholeView)
    {
        [wrapperView setContentView:wormholeView];
        
        NSLog(@"ciro debug:[WormholeEvent DidAppear]. wormholeId:%@", wormholeId);
        NSInteger index = ((HippyWormholeItem *)wormholeView).businessIndex;
        if (wormholeId.length > 0)
        {
            [self notifyWormholeEvent:HippyWormholeEventViewDidAppear extraData:@{@"wormholeId": wormholeId, @"index": @(index)}];
        }
    }
}

#pragma mark - For Native Interfaces

/// 根据原始数据生成一个WormholeViewModel
- (HippyWormholeViewModel *)wormholeViewModelWithRawData:(NSDictionary *)rawData
{
    HippyAssert(![NSThread isMainThread], @"Wormhole ViewModel should not be created on Main Thread!");
    HippyWormholeViewModel *viewModel = [[HippyWormholeViewModel alloc] initWithParams:rawData];
    [viewModel syncBuild];  //NativeVue同步构建
    return viewModel;
}

/// 添加一个WormholeViewModel至数据源
- (void)enqueueWormholeViewModel:(HippyWormholeViewModel *)viewModel
{
    if (!viewModel || viewModel.wormholeId.length == 0)
    {
        return;
    }
    
    NSString *wormholeId = viewModel.wormholeId;
    if ([_wormholeViewModelKeys containsObject:wormholeId])
    {
        // 已经存在
        return;
    }
    
    [_wormholeViewModelKeys addObject:wormholeId];
    [_wormholeViewModelDict setObject:viewModel forKey:wormholeId];
}

/// 移除一个WormholeViewModel从数据源
- (void)dequeueWormholeViewModel:(HippyWormholeViewModel *)viewModel
{
    if (!viewModel || viewModel.wormholeId.length == 0)
    {
        return;
    }
    
    NSString *wormholeId = viewModel.wormholeId;
    
    [_wormholeViewModelKeys removeObject:wormholeId];
    [_wormholeViewModelDict removeObjectForKey:wormholeId];
}

#pragma mark - Handle Notifications

- (void)applicationBecomeActive:(NSNotification *)notification
{
    [self notifyWormholeEvent:HippyWormholeEventEnterForeground extraData:@{}];
}

- (void)applicationEnterBackground:(NSNotification *)notification
{
    [self notifyWormholeEvent:HippyWormholeEventEnterBackground extraData:@{}];
}

@end
