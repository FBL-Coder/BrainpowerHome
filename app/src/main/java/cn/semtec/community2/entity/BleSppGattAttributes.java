/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.semtec.community2.entity;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class BleSppGattAttributes {
    private static HashMap<String, String> attributes = new HashMap<String, String>();

    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

    //B-0002/B-0004
//    Service UUID锛歠ee0
//    Notify锛歠ee1
//    Write:fee1
    public static String BLE_SPP_Service_0 = "0000fee0-0000-1000-8000-00805f9b34fb";
    public static String BLE_SPP_Notify_Characteristic_0 = "0000fee1-0000-1000-8000-00805f9b34fb";
    public static String  BLE_SPP_Write_Characteristic_0 = "0000fee1-0000-1000-8000-00805f9b34fb";

    //B-0006 / TL8266 Use
//    Service UUID锛�1910
//    Notify锛�2B10
//    Write:2B11
      public static String BLE_SPP_Service = "00001910-0000-1000-8000-00805f9b34fb";
      public static String BLE_SPP_Notify_Characteristic = "00002B10-0000-1000-8000-00805f9b34fb";
      public static String BLE_SPP_Write_Characteristic = "00002B11-0000-1000-8000-00805f9b34fb";

    static {
        //B-0002/B-0004 SPP Service
        attributes.put(BLE_SPP_Service_0, "BLE SPP Service_0");
        attributes.put(BLE_SPP_Notify_Characteristic_0, "BLE SPP Notify Characteristic_0");
        attributes.put(BLE_SPP_Write_Characteristic_0, "BLE SPP Write Characteristic_0");

        //B-0006/TL-8266 SPP Service
        attributes.put(BLE_SPP_Service, "BLE SPP Service");
        attributes.put(BLE_SPP_Notify_Characteristic, "BLE SPP Notify Characteristic");
        attributes.put(BLE_SPP_Write_Characteristic, "BLE SPP Write Characteristic");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
