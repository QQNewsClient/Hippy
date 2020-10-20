//
//  HippyWormholeWrapperView.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HippyWormholeProtocol.h"

NS_ASSUME_NONNULL_BEGIN

@class HippyNativeVueViewModel;
@class HippyVirtualWormholeNode;

#pragma mark - Delegate
@protocol HippyWormholeWrapperViewDelegate  <NSObject>

- (void)wrapView:(HippyWormholeWrapperView * _Nullable)wrapView didChangedSize:(CGSize)size;

- (void)wrapView:(HippyWormholeWrapperView * _Nullable)wrapView didRemoveNativeView:(UIView *)nativeView;

@end

#pragma mark - Interface
@interface HippyWormholeWrapperView : UIView<HippyWormholeProtocol>

@property(nonatomic, weak) id<HippyWormholeWrapperViewDelegate> delegate;
@property(nonatomic, weak) HippyVirtualWormholeNode *wormholeNode;

- (instancetype)initWithWormholeId:(NSString *)wormholeId natvieView:(UIView * _Nullable)nativeView;
- (instancetype)initWithFrame:(CGRect)frame NS_UNAVAILABLE;
- (instancetype)init NS_UNAVAILABLE;

- (void)setContentView:(UIView *)contentView;
- (void)clearContentView;


@end


NS_ASSUME_NONNULL_END
