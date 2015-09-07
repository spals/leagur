package store

import "github.com/satori/go.uuid"

type Store interface {
	All(entity string, key string) []map[string]string

	Read(entity string, key string, id uuid.UUID) map[string]string

	Update(entity string, key string, id uuid.UUID, entry map[string]string) map[string]string

	Create(entity string, key string, entry map[string]string) (map[string]string, uuid.UUID)

	Delete(entity string, key string, id uuid.UUID)
}

type HyperDex struct {
}

func (hyperdex HyperDex) All(entity string, key string) []map[string]string {
	// TODO: Read all records on entity from HyperDex using key
	return make([]map[string]string, 0)
}

func (hyperdex HyperDex) Read(entity string, key string, id uuid.UUID) map[string]string {
	// TODO: Read record with id on entity from HyperDex using key
	return make(map[string]string)
}

func (hyperdex HyperDex) Update(entity string, key string, id uuid.UUID, entry map[string]string) map[string]string {
	// TODO: Update record with id on entity from HyperDex using key
	return make(map[string]string)
}

func (hyperdex HyperDex) Create(entity string, key string, entry map[string]string) (map[string]string, uuid.UUID) {
	// TODO: Create record with id on entity from HyperDex using key
	return make(map[string]string), uuid.Nil
}

func (hyperdex HyperDex) Delete(entity string, key string, id uuid.UUID) {
	// TODO: Delete record with id on entity from HyperDex using key
}
