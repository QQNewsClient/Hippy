//
//  HippyNativeVueManager.m
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#import "HippyNativeVueManager.h"
#import "NSObject+NativeVue.h"

@interface HippyNativeVueManager()

@property(atomic, strong) NSDictionary * vueDomMap;

@end

@implementation HippyNativeVueManager


+ (instancetype)shareInstance{
    static HippyNativeVueManager * instance ;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [HippyNativeVueManager new];
    });
    return instance;
}

- (instancetype)init
{
    self = [super init];
    if (self) {
    }
    return self;
}

- (BOOL)loadResource:(NSData *)resource{
    //resource to json
    NSAssert([resource isKindOfClass:[NSData class]], @"resource is not NSData");
    NSDictionary * vueDomMap = [NSJSONSerialization JSONObjectWithData:resource options:NSJSONReadingMutableContainers error:nil];
    NSAssert([vueDomMap isKindOfClass:[NSDictionary class]], @"vueDomMap is not NSDictionary");
    if ([vueDomMap isKindOfClass:[NSDictionary class]]) {
        self.vueDomMap = vueDomMap;
    }
    return [vueDomMap isKindOfClass:[NSDictionary class]];
}

- (NSString *)jsonDomWithTemplateId:(NSString *)templatedId{
    if ([templatedId isKindOfClass:[NSString class]]) {
        NSString * vueDomString = self.vueDomMap[templatedId];
        return vueDomString;
    }
    return nil;
}

- (NSDictionary *)virtualDomWithJsonDom:(NSString *)jsonDom domData:(NSDictionary *)domData{
    return [self.handler virtualDomWithJsonDom:jsonDom domData:domData];
}

- (void)registerGlobalVaribleWithKey:(NSString *)key value:(NSObject *)value{
    
    [self.handler registerGlobalVaribleWithKey:key value:value];
    
    
}





@end
