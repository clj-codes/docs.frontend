# docs.frontend
Frontend SPA for docs.clj.codes

## Config `src/codes/clj/docs/frontend/config.cljs`
This file has two main functions:
- Define mantine.dev base theme
- Set configuration differences between local and deployed versions
  - `:base-url` base url that the http client will use to build the backend requests
  - `:github` informations used to do the login with github like `:client-id` and `:redirect-uri`
- For the configuration values above, their values may be redefined at compile time if their corresponding environment 
variables are present: `BASE_URL`, `CLIENT_ID` and `REDIRECT_URI`.

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
