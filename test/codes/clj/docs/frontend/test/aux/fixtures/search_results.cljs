(ns codes.clj.docs.frontend.test.aux.fixtures.search-results)

(def search-results
  [{:id "org.clojure/clojure/clojure.core.server/servers/0"
    :name "servers"
    :type :definition}
   {:doc "Stop server with name or use the server-name from *session* if none supplied.\n  Returns true if server stopped successfully nil if not found or throws if\n  there is an error closing the socket."
    :id "org.clojure/clojure/clojure.core.server/stop-server/0"
    :name "stop-server"
    :type :definition}
   {:doc "Start a socket server given the specified opts:\n    :address Host or address string defaults to loopback address\n    :port Port integer required\n    :name Name required\n    :accept Namespaced symbol of the accept function to invoke required\n    :args Vector of args to pass to accept function\n    :bind-err Bind *err* to socket out stream? defaults to true\n    :server-daemon Is server thread a daemon? defaults to true\n    :client-daemon Are client threads daemons? defaults to true\n   Returns server socket."
    :id "org.clojure/clojure/clojure.core.server/start-server/0"
    :name "start-server"
    :type :definition}
   {:doc "Stop all servers ignores all errors and returns nil."
    :id "org.clojure/clojure/clojure.core.server/stop-servers/0"
    :name "stop-servers"
    :type :definition}
   {:doc "Start all servers specified in the system properties."
    :id "org.clojure/clojure/clojure.core.server/start-servers/0"
    :name "start-servers"
    :type :definition}
   {:doc "Socket server support"
    :id "org.clojure/clojure/clojure.core.server"
    :name "clojure.core.server"
    :type :namespace}])
