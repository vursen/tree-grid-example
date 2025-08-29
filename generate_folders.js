

import fs from 'node:fs';

const OUT = 'src/main/resources/data.sql';
const TOP = 10000;
const CHILDREN1 = 5;
const CHILDREN2 = 2;
const BATCH_SIZE = 1000; // Number of rows per INSERT

let id = 1;
let rows = [];
let total = 0;

const stream = fs.createWriteStream(OUT, { flags: 'w' });
stream.write('-- Batched INSERTs for folders\n');

function flushRows() {
  if (rows.length === 0) return;
  stream.write('INSERT INTO folders (id, name, parent_id) VALUES\n');
  for (let i = 0; i < rows.length; i++) {
    stream.write(rows[i]);
    if (i < rows.length - 1) {
      stream.write(',\n');
    } else {
      stream.write(';\n');
    }
  }
  rows = [];
}

for (let i = 0; i < TOP; i++) {
  rows.push(`(${id}, 'Folder ${i}', NULL)`);
  total++;
  const parent1 = id;
  id++;

  for (let j = 0; j < CHILDREN1; j++) {
    rows.push(`(${id}, 'Folder ${i}-${j}', ${parent1})`);
    total++;
    const parent2 = id;
    id++;

    for (let k = 0; k < CHILDREN2; k++) {
      rows.push(`(${id}, 'Folder ${i}-${j}-${k}', ${parent2})`);
      total++;
      id++;
      if (rows.length >= BATCH_SIZE) flushRows();
    }
    if (rows.length >= BATCH_SIZE) flushRows();
  }
  if (rows.length >= BATCH_SIZE) flushRows();
}
flushRows();
stream.end();

console.log(`SQL generated at ${OUT} with ${total} rows.`);
