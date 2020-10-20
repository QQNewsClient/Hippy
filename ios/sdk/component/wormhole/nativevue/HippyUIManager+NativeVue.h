//
//  HippyUIManager+NativeVue.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyUIManager+Private.h"
#import "HippyUIManager.h"
#import "HippyNativeVueViewModel.h"
@class HippyComponentData;

NS_ASSUME_NONNULL_BEGIN

@interface HippyUIManager (NativeVue)

//- (HippyNativeVueViewModel *)nv_createModelWithTemplateId:(NSString *)templateId templateData:(NSDictionary *)templateData;

- (HippyComponentData *)componentDataWithViewName:(NSString *)viewName;

- (NSDictionary *)componentDataByName;

@end

NS_ASSUME_NONNULL_END
