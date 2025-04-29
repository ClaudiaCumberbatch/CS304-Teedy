module.exports = function (grunt) {
    grunt.loadNpmTasks('grunt-apidoc');
    grunt.initConfig({
      apidoc: {
        myapp: {
          src: 'src/',       // 包含 API 注释的源代码目录
          dest: 'docs/',     // 生成的文档输出目录
          options: {
            includeFilters: [".*\\.js$"],  // 解析的文件类型
            // 添加以下配置确保资源路径正确（根据 apidoc 版本可能需要）
            copy: true,    // 复制静态资源（如 JS/CSS）
          }
        }
      }
    });
    grunt.loadNpmTasks('grunt-apidoc');
    grunt.registerTask('default', ['apidoc']);
    grunt.registerTask('prod', ['apidoc']);
  };