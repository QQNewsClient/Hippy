
//  HippyNativeVueViewModel.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@class HippyNativeVueViewModel;
@class HippyUIManager;

NS_ASSUME_NONNULL_BEGIN

typedef void (^HippyNVBuildCompletion)(HippyNativeVueViewModel * model);

@interface HippyNativeVueViewModel : NSObject

@property(nonatomic, assign, readonly) BOOL buildCompleted;

- (instancetype)initWithJsonDom:(NSString *)jsonDom domData:(NSDictionary *)domData context:(HippyUIManager *)context;

- (void)syncBuild;
- (void)asyncBuildWithCompletion:(HippyNVBuildCompletion)completion;

//getView when buildCompleted
- (UIView * _Nullable)view;

- (CGFloat)viewWidth;
- (CGFloat)viewHeight;

@end

NS_ASSUME_NONNULL_END
