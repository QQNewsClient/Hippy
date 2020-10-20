//
//  HippyWormholeItem.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HippyWormholeProtocol.h"

@class HippyBridge;

@interface HippyWormholeItem : UIView<HippyWormholeProtocol>

- (instancetype)initWithBridge:(HippyBridge *)bridge;

@property(nonatomic, strong) NSDictionary *params;

@property(nonatomic, assign) NSInteger businessIndex;

@end
