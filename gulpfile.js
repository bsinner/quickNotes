const { src, dest, parallel, series } = require("gulp");
const minifyCss = require("gulp-clean-css");
const rename = require("gulp-rename")
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
        .pipe(dest("src/main/webapp/dist"));
}

exports.default = series(cleanDist, css);
