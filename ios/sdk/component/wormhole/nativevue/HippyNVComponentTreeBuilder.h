//
//  HippyNVComponentTreeBuilder.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HippyComponentData.h"
@class HippyShadowView;
@class HippyUIManager;
@class HippyNVComponent;
NS_ASSUME_NONNULL_BEGIN

@interface HippyNVComponentTreeBuilder : NSObject

- (instancetype)initWithContext:(HippyUIManager *)context;

- (HippyNVComponent *)buildComponentTreeWithVirtualDom:(NSDictionary *)virtualDom;

- (HippyComponentData * _Nullable)componentDataWithViewName:(NSString *)viewName;

- (NSNumber * _Nullable)rootTag;

- (void)addUIBlock:(HippyViewManagerUIBlock)block;

- (HippyUIManager *)uiManager;

- (void)addBridgeTransactionListeners:(id<HippyComponent>)listner;

- (void)registerViewWithTag:(NSNumber *)tag view:(UIView *)view component:(HippyNVComponent *)component;

- (UIView * _Nullable)view;

@end

NS_ASSUME_NONNULL_END
