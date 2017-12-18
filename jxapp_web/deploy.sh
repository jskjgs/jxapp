npm run build
git add .
git commit -m 'deploy'
git pull origin master
git push origin master
expect ./deploy.exp
