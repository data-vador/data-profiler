#!/bin/bash
# deploy.sh

echo "🚀 Déploiement de l'application CSV Profiler"

# Vérifier que Docker est installé
if ! command -v docker &> /dev/null; then
    echo "❌ Docker n'est pas installé"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose n'est pas installé"
    exit 1
fi

# Build du backend Spring Boot
echo "📦 Build du backend Spring Boot..."
cd backend
./mvnw clean package -DskipTests
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du build du backend"
    exit 1
fi
cd ..

# Build du frontend Angular
echo "📦 Build du frontend Angular..."
cd frontend
npm ci
npm run build --prod
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du build du frontend"
    exit 1
fi
cd ..

# Arrêter les conteneurs existants
echo "🛑 Arrêt des conteneurs existants..."
docker-compose down

# Construire et démarrer les nouveaux conteneurs
echo "🐳 Construction et démarrage des conteneurs..."
docker-compose up --build -d

# Vérifier que les services sont démarrés
echo "⏳ Vérification du démarrage des services..."
sleep 30

# Test de santé du backend
backend_health=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/health)
if [ "$backend_health" == "200" ]; then
    echo "✅ Backend opérationnel"
else
    echo "❌ Backend non opérationnel (HTTP $backend_health)"
fi

# Test de santé du frontend
frontend_health=$(curl -s -o /dev/null -w "%{http_code}" http://localhost)
if [ "$frontend_health" == "200" ]; then
    echo "✅ Frontend opérationnel"
else
    echo "❌ Frontend non opérationnel (HTTP $frontend_health)"
fi

echo "🎉 Déploiement terminé!"
echo "📱 Frontend: http://localhost"
echo "🔧 Backend API: http://localhost:8080/api"

# Afficher les logs en temps réel
echo "📊 Logs en temps réel (Ctrl+C pour arrêter):"
docker-compose logs -f