//
//  HippyWormholeBaseShadowView.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyShadowView.h"
#import "HippyWormholeProtocol.h"

NS_ASSUME_NONNULL_BEGIN
@class HippyWormholeViewModel;

@interface HippyWormholeBaseShadowView : HippyShadowView<HippyWormholeProtocol>

@property (nonatomic, strong) HippyWormholeViewModel *viewModel;

@end

NS_ASSUME_NONNULL_END
