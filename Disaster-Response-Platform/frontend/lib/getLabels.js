import { readFile } from "fs/promises";
import path from "path";

async function readLabels(language) {
  const langPath = path.join(process.cwd(), `lib/labels/${language}.json`);
  const contents = await readFile(langPath);
  return JSON.parse(contents);
}

var memo = {};

export default async function getLabels(language) {
  if (!language) {
    language = "TR";
  }
  if (!(language in memo)) {
    memo[language] = readLabels(language);
  }
  return memo[language];
}