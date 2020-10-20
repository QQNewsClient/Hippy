//
//  HippyNVComponent.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HippyShadowView.h"
#import "HippyVirtualNode.h"
@class HippyNVComponentTreeBuilder;
NS_ASSUME_NONNULL_BEGIN

@interface HippyNVComponent : NSObject

@property(nonatomic, strong, nonnull, readonly) NSNumber *tag;
@property(nonatomic, strong, nonnull, readonly) UIView *view;
@property(nonatomic, strong, nonnull, readonly) NSString *viewName;
@property(nonatomic, strong, nonnull, readonly) HippyShadowView *shadowView;
@property(nonatomic, strong, nonnull, readonly) HippyVirtualNode *virtualNode;
@property(nonatomic, strong, nullable, readonly) HippyNVComponent *parentComponent;

- (instancetype)initWithJsonData:(NSDictionary *)jsonData builder:(HippyNVComponentTreeBuilder *)builder;

- (void)insertSubcomponent:(HippyNVComponent *)subcomponent atIndex:(NSInteger)index;

- (CGRect)frame;

@end

NS_ASSUME_NONNULL_END
