package main

import (
	"fmt"
	"time"
	"github.com/satori/go.uuid"
)


type player struct {
	id uuid
	name string
	email string
	phone string
	gender int
	teams []uuid
}

const (
	male
	female
)

type user struct {
	id uuid
	player uuid
}

const (
	owner
	captain
	player
)

type team struct {
	id uuid
	name string
	roles []uuid
}

type role struct {
	id uuid
	team uuid
	player uuid
	role int
}

type location struct {
	id uuid
	name string
	x float64
	y float64
}

const (
	yes
	no
	maybe
	late
)

type status struct {
	player uuid
	status int
}

type event struct {
	team team
	date time
	location uuid
	players []status
}

