//
//  NSObject+NativeVue.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NVDeallocObject : NSObject




@end

@interface NSObject (NativeVue)

@property(nonatomic,strong) NSObject * dy_leakObject;

@property(nonatomic,strong) NSString * hippy_requestUrl;

@property(nonatomic,assign) BOOL hippy_from_nativevue;


@end

@interface NSDictionary (NativeVue)


- (NSString *)nv_convertToJsonStringWithOption:(NSJSONWritingOptions)option;

- (NSString *)nv_convertToJsonString;


@end

@interface NSString (NativeVue)


- (NSDictionary *)nv_stringToDictionary;

@end
NS_ASSUME_NONNULL_END
