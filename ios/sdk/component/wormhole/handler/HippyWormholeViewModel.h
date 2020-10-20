//
//  HippyWormholeViewModel.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HippyWormholeProtocol.h"
#import "HippyWormholeWrapperView.h"
#import "HippyNativeVueViewModel.h"
NS_ASSUME_NONNULL_BEGIN
@class HippyWormholeViewModel;
@protocol HippyWormholeViewModelDelegate  <NSObject>

@optional

- (void)wormholeWillAppear:(HippyWormholeViewModel *)viewModel;

- (void)wormholeWillDisappear:(HippyWormholeViewModel *)viewModel;

- (void)wormholeDidOnClick:(HippyWormholeViewModel *)viewModel;

- (void)wormholeViewModel:(HippyWormholeViewModel *)viewModel didChangedSize:(CGSize)size;

/*!
 @brief 更新wormhole
 */
- (void)wormholeModelUpdate:(HippyWormholeViewModel *)viewModel;

@end

@class HippyWormholeBusinessHandler;
@class HippyVirtualNode;
@class HippyWormholeBaseShadowView;
@class HippyBridge;
@interface HippyWormholeViewModel : NSObject<HippyWormholeProtocol>

@property (nonatomic, strong, readonly) NSDictionary *params;
@property (nonatomic, weak) id<HippyWormholeViewModelDelegate> delegate;
@property (nonatomic, strong, nullable, readonly) HippyWormholeWrapperView *view;
@property (nonnull, strong) NSIndexPath *indexPath; // for Native case only

- (instancetype)initWithParams:(NSDictionary *)params;
- (instancetype)init NS_UNAVAILABLE;

- (void)syncBuild;
- (void)asyncBuildWithCompletion:(HippyNVBuildCompletion)completion;

- (CGFloat)viewWidth;
- (CGFloat)viewHeight;

- (void)attachViewModelPropsToShadowView:(HippyWormholeBaseShadowView *)shadowView withOriginProps:(NSMutableDictionary *)originProps;
- (void)updateShadowView:(HippyWormholeBaseShadowView *)shadowView onWormholeSizeChanged:(CGSize)size;

#pragma mark - For Native Interfaces

- (void)wormholeWillAppear;

- (void)wormholeWillDisappear;

- (NSDictionary *)toJSONObject;

@end

NS_ASSUME_NONNULL_END
