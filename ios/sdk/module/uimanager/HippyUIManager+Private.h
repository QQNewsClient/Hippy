//
//  HippyUIManager+private.h
//  hippy
//
//  Copyright © 2020 Tencent. All rights reserved.
//

#ifndef HippyUIManager_private_h
#define HippyUIManager_private_h
#import <UIKit/UIKit.h>
#import "HippyUIManager.h"

@interface HippyUIManager () {
    // Keyed by viewName
    NSDictionary *_componentDataByName;
}

@end




#endif /* HippyUIManager_private_h */
