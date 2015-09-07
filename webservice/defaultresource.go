package webservice

import (
	"encoding/json"
	"net/http"
	"github.com/satori/go.uuid"
	"github.com/spals/leagur/store"
)

type DefaultResource struct {
	store store.Store
}

func (r *DefaultResource) Path() string {
	return "/:entity"
}

func (r *DefaultResource) Delete(entity string, key string, id uuid.UUID) (int, string) {
	r.store.Delete(entity, key, id)
	return http.StatusOK, "entry deleted"
}

func (r *DefaultResource) Get(entity string, key string, id uuid.UUID) (int, string)  {
	entry := r.store.Read(entity, key, id)
	bytes, err := json.Marshal(entry)

	if err != nil {
		return http.StatusInternalServerError, "entry was not marshalled"
	}

	return http.StatusOK, string(bytes)
}

func (r *DefaultResource) Put(entity string, key string, id uuid.UUID, entry string) (int, string)  {
	var objmap map[string]string
	err := json.Unmarshal([]byte(entry), &objmap)

	if err != nil {
		return http.StatusInternalServerError, "entry was not unmarshalled"
	}

	objmap = r.store.Update(entity, key, id, objmap)
	bytes, err := json.Marshal(entry)

	if err != nil {
		return http.StatusInternalServerError, "entry was not marshalled"
	}

	return http.StatusOK, string(bytes)
}

func (r *DefaultResource) Post(entity string, key string, entry string) (int, string, uuid.UUID)  {
	var objmap map[string]string
	err := json.Unmarshal([]byte(entry), &objmap)

	if err != nil {
		return http.StatusInternalServerError, "entry was not unmarshalled", uuid.Nil
	}

	objmap, id := r.store.Create(entity, key, objmap)
	bytes, err := json.Marshal(objmap)

	if err != nil {
		return http.StatusInternalServerError, "entry was not marshalled", uuid.Nil
	}

	return http.StatusOK, string(bytes), id
}
