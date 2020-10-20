//
//  HippyUIManager+NativeVue.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyUIManager+NativeVue.h"

@implementation HippyUIManager (NativeVue)

- (HippyComponentData *)componentDataWithViewName:(NSString *)viewName{
    return [self->_componentDataByName objectForKey:viewName];
}

//- (HippyNativeVueViewModel *)nv_createModelWithTemplateId:(NSString *)templateId templateData:(NSDictionary *)templateData{
//    return [[HippyNativeVueViewModel alloc] initWithTemplateId:templateId templateData:templateData context:self];
//}

- (NSDictionary *)componentDataByName{
    return _componentDataByName;
}
@end
