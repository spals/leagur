package webservice

import (
	"net/http"
	"github.com/satori/go.uuid"
	"fmt"
	"net/url"
	"github.com/spals/leagur/store"
)

type WebService interface {
	Path() string

	Delete(entity string, key string, id uuid.UUID) (int, string)

	Get(entity string, key string, id uuid.UUID) (int, string)

	Post(entity string, key string, id uuid.UUID, entry string) (int, string, uuid.UUID)

	Put(entity string, key string, entry string) (int, string)
}

func Handler(res http.ResponseWriter, req *http.Request) {
	handler(res, req, DefaultResource{store: store.HyperDex{}})
}

func handler(res http.ResponseWriter, req *http.Request, resource DefaultResource) {
	fmt.Printf("[%s] %s\n", req.Method, req.URL.Path)
	query := req.URL.Query()

	entry := query.Get("entry")
	entity := query.Get("entity")
	key := query.Get("key")

	url.Parse(req.URL.Path)

	if entity == "" {
		res.WriteHeader(http.StatusInternalServerError)
		return
	}

	if key == "" {
		res.WriteHeader(http.StatusBadRequest)
		return
	}

	// post is the only one that doesn't require an id
	id, err := uuid.FromString(query.Get("id"))
	if err != nil && req.Method != "POST" {
		res.WriteHeader(http.StatusBadRequest)
		return
	}
	fmt.Printf("%s %s %s %s\n", id, entry, entity, key)

	if req.Method == "DELETE" {

	} else if req.Method == "GET" {

	} else if req.Method == "POST" {

	} else if req.Method == "PUT" {

	} else {
		fmt.Errorf("Cannot handle HTTP method %s", req.Method)
	}
}