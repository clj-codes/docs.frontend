# docs.frontend
Frontend SPA for docs.clj.codes

## Project
[Check the project backlog, issues and ongoing tasks](https://github.com/orgs/clj-codes/projects/2)

## Commands

### Watch
Start shadow-cljs watching and serving app and tests
```bash
npm start
```
> Main App available at http://localhost:5000  
> Tests available at http://localhost:5002  

### Tests
Compile with shadow-cljs and run tests in node with karma
```bash
npm test
```
#### Jsdom (under evaluation)
Compile with shadow-cljs and run tests in node with jsdom
```bash
npm run test:node
```

### Release
Build the release package to production deploy
```bash
npm run release
```
