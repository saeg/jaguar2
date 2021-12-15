/* *******************************
 * Stylize the anchored code line
 * ******************************/

const fileName = document.URL;
const startLine = fileName.indexOf("#");

if (startLine !== -1) {
    focusCodeLine(fileName.substring(startLine + 1, fileName.length));
}

function focusCodeLine(id) {
    const line = document.getElementById(id);
    if (line === null) {
        throw Error("Couldn't find element with id '" + id +"'.");
    }

    const parent = line.parentElement
    parent.id = "focusedLineParent";

    const sign = document.createElement("figure");
    sign.id = "focusedLine";

    parent.prepend(sign);
}