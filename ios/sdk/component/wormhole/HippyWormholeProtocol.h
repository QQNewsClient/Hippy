//
//  HippyWormholeProtocol.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreGraphics/CoreGraphics.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, HippyWormholeEvent) {
    HippyWormholeEventViewDidAppear,
    HippyWormholeEventViewDidDisappear,
    HippyWormholeEventEnterForeground,
    HippyWormholeEventEnterBackground,
};

@class HippyVirtualNode, UIView, HippyBridge, HippyVirtualWormholeNode, HippyShadowView,  HippyWormholeWrapperView, HippyVirtualWormholeNode, HippyWormholeViewModel;

@protocol HippyWormholeProtocol <NSObject>

@property(nonatomic, copy) NSString *wormholeId;

@end

#pragma mark - HippyWormholeDataSource
@protocol HippyWormholeDataSource <NSObject>

@optional

/**
 @wormholeId 虫洞id
 @nvOverlayView NativeView浮层（加速首屏渲染）
 */
- (HippyWormholeWrapperView *)wormholeViewWithWormholeId:(NSString *)wormholeId
                                         nvOverlayView:(UIView * _Nullable)nvOverlayView;

@end

#pragma mark - HippyWormholeDelegate
@protocol HippyWormholeDelegate <NSObject>

@optional

- (void)didReceiveWormholeData:(NSDictionary *)data;

- (void)didCreateWormholeNode:(HippyVirtualWormholeNode *)wormholeNode userInfo:(NSDictionary *)userInfo;

- (void)didCreatedViewModel:(HippyWormholeViewModel *)wholeViewModel;

/*!
 @brief 更新wormholeNode
 */
- (void)didUpdatedWormholeNode:(HippyVirtualWormholeNode *)wormholeNode
                      userInfo:(NSDictionary *)userInfo;

@end

NS_ASSUME_NONNULL_END
