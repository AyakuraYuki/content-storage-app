// https://eslint.org/docs/user-guide/configuring

module.exports = {
  root: true,

  parserOptions: {
    parser: 'babel-eslint'
  },

  env: {
    browser: true
  },

  extends: [
    // // https://github.com/vuejs/eslint-plugin-vue#priority-a-essential-error-prevention
    // // consider switching to `plugin:vue/strongly-recommended` or `plugin:vue/recommended` for stricter rules.
    'plugin:vue/essential',
    // // https://github.com/standard/standard/blob/master/docs/RULES-en.md
    'standard'
  ],

  // required to lint *.vue files
  plugins: [
    'vue'
  ],

  // add your custom rules here
  rules: {
    // allow async-await
    'generator-star-spacing': 'off',
    // allow debugger during development / 在开发模式中允许 debugger
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'use-isnan': 'error',
    // 强制 getter 函数中出现 return 语句，但不强制需要提供期望的返回
    'getter-return': ['error', { allowImplicit: true }],
    // 禁止 function 定义中出现重名参数
    'no-dupe-args': 'error',
    // 禁止出现重复的 case 标签
    'no-duplicate-case': 'error',
    // 强制所有控制语句使用一致的括号风格
    'curly': 'error',
    // 强制使用一致的缩进，缩进值为 2 个空格
    'indent': ['off', 2],
    // 不要求使用分号代替 ASI
    'semi': 'off',
    'semi-spacing': ['error', { 'before': false, 'after': true }],
    'no-extra-semi': 'error',
    'comma-spacing': ['error', { 'before': false, 'after': true }],
    // 禁止在 return、throw、continue 和 break 语句之后出现不可达代码
    'no-unreachable': 'error',
    // 强制在 function 的左括号之前使用一致的空格，但允许定义名称的 function 紧接左括号
    "space-before-function-paren": ["error", {
      "anonymous": "always",
      "named": "never",
      "asyncArrow": "always"
    }]
  }
};
