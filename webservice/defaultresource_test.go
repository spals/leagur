package webservice
import (
	"github.com/satori/go.uuid"
	"testing"
)

func TestGet(t *testing.T) {
	resource := DefaultResource{store: MockStore{}}
	_, actual := resource.Get("enitity", "key", uuid.Nil)

	if actual != "{\"k1\":\"v1\",\"k2\":\"v2\"}" {
		t.Error(actual)
	}
}

type MockStore struct {
}

func (mockstore MockStore) All(entity string, key string) []map[string]string {
	// TODO: Read all records on entity from MockStore using key
	return make([]map[string]string, 0)
}

func (mockstore MockStore) Read(entity string, key string, id uuid.UUID) map[string]string {
	x := make(map[string]string)
	x["k1"] = "v1"
	x["k2"] = "v2"
	return x
}

func (mockstore MockStore) Update(entity string, key string, id uuid.UUID, entry map[string]string) map[string]string {
	// TODO: Update record with id on entity from MockStore using key
	return make(map[string]string)
}

func (mockstore MockStore) Create(entity string, key string, entry map[string]string) (map[string]string, uuid.UUID) {
	// TODO: Create record with id on entity from MockStore using key
	return make(map[string]string), uuid.Nil
}

func (mockstore MockStore) Delete(entity string, key string, id uuid.UUID) {
	// TODO: Delete record with id on entity from MockStore using key
}
