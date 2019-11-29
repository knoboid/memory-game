var gulp = require('gulp');
const { exec } = require('child_process');

function runTests(cb) {
    console.log("Starting tests.");
    exec('mvn -e test', (err, stdout, stderr) => {
        if (err) {
          console.error(err);
          // if (stderr) {console.log(`stderr: ${stderr}`);}
          // if (stdout) {console.log(`stdout: ${stdout}`);}
        } else {
          if (stderr) {console.log(`stderr: ${stderr}`);}
          if (stdout) {console.log(`stdout: ${stdout}`);}
        }
    });
    cb();
  }
  
  exports.watch = function() {
      gulp.watch(['**/*.*', '!settings/**', '!target/**'], runTests);
  };
  