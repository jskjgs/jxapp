git checkout dev
npm run build
git add .
git commit -m 'deploy'
git pull origin
git push origin
expect ./deploy.exp
