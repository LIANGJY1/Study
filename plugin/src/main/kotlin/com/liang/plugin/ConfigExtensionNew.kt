package com.liang.plugin

open class ConfigExtensionNew {
    public var logTag: String = "cs"
    public var includePackages: Array<String> = arrayOf("com.example.study.homepage.activity")
    public var excludePackages: Array<String> = arrayOf()
    public var includeMethods: Array<String> = arrayOf()
    public var excludeMethods: Array<String> = arrayOf()
    public var limitTime: Long = 500
}