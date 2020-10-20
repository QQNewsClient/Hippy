//
//  HippyWormholeFactory.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HippyBridge.h"

@class HippyWormholeWrapperView;
@class HippyVirtualWormholeNode;
@class HippyWormholeViewModel;

@interface HippyWormholeFactory : NSObject

- (void)setWormholeWrapperView:(HippyWormholeWrapperView *)wrapperView forWormholeId:(NSString *)wormholeId;

- (void)setWormholeNode:(HippyVirtualWormholeNode *)node forWormholeId:(NSString *)wormholeId;

- (void)setWormholeViewModel:(HippyWormholeViewModel *)model forWormholeId:(NSString *)wormholeId;

- (void)removeWormholeWrapperView:(NSString *)wormholeId;

- (void)removeWormholeNode:(NSString *)wormholeId;

- (void)removeWormholeViewModel:(NSString *)wormholeId;


- (HippyVirtualWormholeNode *)wormholeNodeForWormholeId:(NSString *)wormholeId;

- (HippyWormholeWrapperView *)wormholeWrapperViewForWormholeId:(NSString *)wormholeId;

- (HippyWormholeViewModel *)wormholeViewModelForWormholeId:(NSString *)wormholeId;

- (void)clear;

@end

@interface HippyBridge (WormholeFactory)

@property(nonatomic, strong) HippyWormholeFactory *wormholeFactory;

@end
