package com.hw.net.exception

import java.io.IOException

/**
 * @author David
 * @date 2020/7/1
 * @Copyright   Shanghai Xinke Digital Technology Co., Ltd.
 * @description   异常类
*/

class ApiException(message: String?) : IOException(message)