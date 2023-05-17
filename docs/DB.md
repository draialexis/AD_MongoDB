# About the database schema

## Collections

### trainers collection

* _id: ObjectId
* name: string
    * (_indexed_: would be queried often in a dashboard situation)
* dob: date
* wins: int
* losses: int
* past_opponents: array of ObjectId (references to other trainers)
    * (_indexed_: reflexivity would make deep queries quite slow, so it seems worthwhile)
* pokemongs: array of ObjectId (references to owned pokemongs) + denormalizing on "nickname" and "species"
    * (_indexed_: to improve speed when querying non-denormalized fields)

### pokemongs collection

* _id: ObjectId
* nickname: string
* dob: date
* level: int
* pokedex_id: int
* evo_stage: int
    * (_indexed_: "species" is calculated as evo_track[evo_stage], and would be queried often)
* evo_track: array of strings (therefore "species" is evo_track[evo_stage], and "evo_base" is evo_track[0])
    * (_indexed_: "species" is calculated as evo_track[evo_stage], and xould be queried often)
* is_mega_evolved: boolean
    * **polymorphic**: this field is only here for mature_pokemongs, i.e. pokemongs who have reached their last evo_stage
* trainer: ObjectId? (reference to a trainer) (but can be "wild" instead, if ref is null)
    * (_indexed_: could be queried often in a dashboard situation)
* types: embedded type, or array of embedded types
    * (_indexed_: would be queried often in a dashboard situation)
* move_set: array of ObjectId (references to known moves) + denormalizing on "name"

### moves collection

* _id: ObjectId
* name: string
    * (_indexed_: would be queried often in a dashboard situation)
* category: string (can be "physical", "special", or "status")
* pp: int
* power: int
    * (_indexed_: would be used often in sorts, in a dashboard situation)
* accuracy: int
* type: embedded type
    * (_indexed_: would be queried often in a dashboard situation)


### types collection

* _id: ObjectId
* name: string
    * (_indexed_: would be queried often in a dashboard situation)
* weak_against: array of strings (denormalized type names)
* effective_against: array of strings (denormalized type names)

## Relationships

* trainers.past_opponents: one-to-many and reflexive
    * => referencing
* trainers.pokemongs: one-to-many
    * => referencing + denormalizing on "nickname" and "species"
* pokemongs.trainer: many-to-one
    * => referencing
* pokemongs.types: one-to-few [1;2] but will also need to be queried independently
    * => denormalizing on all fields
* pokemongs.move_set: one-to-few [1;4] but will also need to be queried independently
    * => referencing + denormalizing on  "name"
* moves.type: one-to-one [1;1] but will also need to be queried independently
    * => denormalizing on all fields
* types.weak_against & types.effective_against: one-to-few but reflexive
    * => denormalizing on "name"
	