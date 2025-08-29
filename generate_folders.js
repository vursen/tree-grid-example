import fs from 'node:fs';

const OUT = 'src/main/resources/data.sql';
const TOP = 10000;
const CHILDREN1 = 5;
const CHILDREN2 = 2;

let id = 1;

const stream = fs.createWriteStream(OUT, { flags: 'w' });
stream.write('-- Generating 50,000 folders, each with 5 children, each with 3 children\n');

for (let i = 0; i < TOP; i++) {
  stream.write(`INSERT INTO folders (id, name, parent_id) VALUES (${id}, 'Folder ${i}', NULL);\n`);
  const parent1 = id;
  id++;

  for (let j = 0; j < CHILDREN1; j++) {
    stream.write(`INSERT INTO folders (id, name, parent_id) VALUES (${id}, 'Folder ${i}-${j}', ${parent1});\n`);
    const parent2 = id;
    id++;

    for (let k = 0; k < CHILDREN2; k++) {
      stream.write(`INSERT INTO folders (id, name, parent_id) VALUES (${id}, 'Folder ${i}-${j}-${k}', ${parent2});\n`);
      id++;
    }
  }
}
stream.end();
