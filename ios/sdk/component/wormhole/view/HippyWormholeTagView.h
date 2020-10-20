//
//  HippyWormholeTagView.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <UIKit/UIKit.h>

@class HippyBridge;

@interface HippyWormholeTagView : UIView

@property(nonatomic, strong) NSDictionary *params;

- (void)sendEventToWormholeView:(NSDictionary *)eventObj;

@end
