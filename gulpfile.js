const { src, dest, parallel, series } = require("gulp");
const minifyCss = require("gulp-clean-css");
const minifyJs = require("gulp-uglify");
const babel = require("gulp-babel");
const rename = require("gulp-rename");
const clean = require("gulp-clean");

// Delete contents of dist
function cleanDist() {
    return src("src/main/webapp/dist")
        .pipe(clean());
}

// Minify css
function css() {
    return src("src/main/webapp/css/**/*.css")
        .pipe(minifyCss({ compatibility : "ie8" }))
        .pipe(rename({ suffix : ".min" }))
        .pipe(dest("src/main/webapp/dist/css"));
}

// Compile and minify js
function js() {
    return src("src/main/webapp/js/**/*.js")
        .pipe(babel({ presets : ["@babel/env"] }))
        .pipe(minifyJs())
        .pipe(rename({ suffix : ".min" }))
        .pipe(dest("src/main/webapp/dist/js"));
}

exports.default = series(cleanDist, parallel(css, js));
