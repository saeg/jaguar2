const tableName = "dataTable";
const iptName = "itensPerTable";
const rangeName = "susRange";
const rangeTextName = "rangeText";

const table = document.getElementById(tableName);
const itensSelect = document.getElementById(iptName);
const range = document.getElementById(rangeName);
const rangeText = document.getElementById(rangeTextName);


function nullElements(tableName, table, itensName, itens, rangeName, range, rangeTextName, rangeText) {
  let nElement = [];
  if (!table) nElement.push(tableName);
  if (!itens) nElement.push(itensName);
  if (!range) nElement.push(rangeName);
  if (!rangeText) nElement.push(rangeTextName);

  if (nElement.length === 0) return false;

  if (range !== null) range.disabled = true;
  if (itens != null) itens.disabled = true;
  nElement.forEach(e => { console.error("Couldn't find element with id '" + e + "'.") });
  return true;
}

if (nullElements(tableName, table, iptName, itensSelect, rangeName, range, rangeTextName, rangeText) === false) {
  let selectedPagButton = 1;
  const rows = table.getElementsByTagName('tr');

  startPagination(rows);

  function startPagination(displayedRows) {
    const filter = () => (itensSelect.value != 'all') ? parseInt(itensSelect.value) : displayedRows.length;
    createPagButtons(filter(), displayedRows);
    clickPagination(filter(), 1, displayedRows);
    rangeText.innerHTML = parseFloat(range.value);
  }

  //Display table itens according to their susp values
  function filterSuspiciousness() {
    const filter = parseFloat(range.value);
    rangeText.innerHTML = filter;

    for (let j = 1; j < rows.length; j++) {
      const tds = rows[j].getElementsByTagName('td');
      let sc = document.getElementById("suspColumn");

      if (sc === null){
        console.error("Couldn't find the suspiciousness column.");
        break;
      }

      const item = tds.item(Array.from(sc.parentNode.children).indexOf(sc));

      if (parseFloat(item.textContent) > filter) {
        rows[j].style.display = 'none';
      } else { rows[j].style.display = ''; }
    }

    const displayedRows = Array.from(rows).filter(el => el.style.display != 'none');
    startPagination(displayedRows);
  }


  range.addEventListener("change", filterSuspiciousness, false);
  itensSelect.addEventListener("change", filterSuspiciousness, false);

  //Create the pagination buttons at the pagination div
  function createPagButtons(filter, displayedRows) {
    const s = displayedRows.length / filter;
    const r = displayedRows % filter;
    const pagination = document.getElementById("pagination");
    if (pagination === null){
      console.error("Couldn't find the pagination div.");
      return;
    }
    pagination.innerText = '';

    if (s === 1) return;
    else if (r > 0) s += 1;

    const previous = document.createElement("button");
    previous.className = "pagButton";
    previous.disabled = true;
    previous.innerHTML = "<<";
    previous.id = "pagButtonPrevious";
    pagination.appendChild(previous);

    const next = document.createElement("button");
    next.className = "pagButton";
    next.innerHTML = ">>";
    previous.id = "pagButtonNext";

    for (let i = 0; i < s; i++) {
      // create the buttons
      const pag = document.createElement("button");
      pag.className = "pagButton";
      pag.name = i + 1;
      pag.innerHTML = i + 1;
      pag.disabled = false;
      pag.autocomplete = 'off';
      pag.id = `pagButton${i + 1}`;

      pag.addEventListener("click", function () {
        selectedPagButton = pag.name;

        // organize the disabled status
        const siblings = Array.from(pag.parentElement.children);
        siblings.forEach(s => s === pag ? s.disabled = true : s.disabled = false);

        // organize the table data
        clickPagination(filter, pag.name, displayedRows);

        if (i >= 1) previous.disabled = false;
        else previous.disabled = true;

        if (i >= s - 1) next.disabled = true;
        else next.disabled = false;
      });

      if (i == 0) pag.disabled = true;
      pagination.appendChild(pag);
    }

    if (s <= 1) next.disabled = true;
    pagination.appendChild(next);

    previous.onclick = function () {
      let temp = parseInt(selectedPagButton) - 1;
      document.getElementById(`pagButton${temp}`).click();
    }

    next.onclick = function () {
      let temp = parseInt(selectedPagButton) + 1;
      document.getElementById(`pagButton${temp}`).click();
    }
  }


  function clickPagination(itensPerPage, page, displayedRows) {
    const start = () => page == 1 ? 1 : itensPerPage * (page - 1) + 1;
    const end = start() + itensPerPage;

    for (let i = 1; i < displayedRows.length; i++) {
      if (i >= start() && i < end)
        displayedRows[i].style.display = '';
      else displayedRows[i].style.display = 'none';
    }
  }
}
