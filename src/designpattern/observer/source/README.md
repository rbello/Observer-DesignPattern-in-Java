# Implémentation Source

Une implémentation beaucoup plus évoluée, qui se base sur la réflexion.

**Features**
- Indice de priorité, pour ordonner la propagation aux listeners
- Permet l'interruption de la propagation
- Gestion des exceptions levées par les listeners
- Redirection d'un EventDispatcher vers un autre (décoration)
- Méthodes de trigger asynchrones
- Thread-safe avec le dispatcher EDT de Swing
- Logger

**Forces**
- Permet de spécifier des interfaces claires pour les events
- Encapsulée : la classe EventDispatcher implémente la logique de broadcast, pas besoin de l'implémenter
- Plus obligé d'implémenter une interface pour devenir listener, une même classe peut donc être listener de plusieurs events
- Entièrement générique

**Faiblesses**
- Performances (utilisation de la Reflexion)
- Typage faible, le compilateur ne peut pas détecter les erreurs quand on utilise bind() pour pointer une méthode
- Complexe à comprendre