# About the database schema

## Collections

### trainers collection

* _id: ObjectId
* name: string
    * (_indexed_: would often be queried in a dashboard situation)
* dob: date
* wins: int
* losses: int
* pastOpponents: array of ObjectId (references to other trainers)
    * (_indexed_: reflexivity would make deep queries quite slow, so it seems worthwhile)
* pokemongs: array of ObjectId (references to owned pokemongs) + denormalizing on "nickname" and "species"
    * (_indexed_: to improve speed when querying non-denormalized fields)

### pokemongs collection

* _id: ObjectId
* nickname: string?
* dob: date
* level: int
* pokedexId: int
* evoStage: int
    * (_indexed_: "species" is calculated as evoTrack[evoStage], and would often be queried)
* evoTrack: array of strings (therefore "species" is evoTrack[evoStage], and "evoBase" is evoTrack[0])
    * (_indexed_: "species" is calculated as evoTrack[evoStage], and would be queried often)
* trainer: ObjectId? (reference to a trainer) (but can be "wild" instead, if ref is null)
    * (_indexed_: could be queried often in a dashboard situation)
* types: embedded type, or array of embedded types
    * (_indexed_: would often be queried in a dashboard situation)
* moveSet: array of ObjectId (references to known moves) + denormalizing on "name"

### moves collection

* _id: ObjectId
* name: string
    * (_indexed_: would often be queried in a dashboard situation)
* category: string (can be "physical", "special", or "status")
* power: int
    * (_indexed_: would often be used in sorts, in a dashboard situation)
* accuracy: int
* type: embedded type
    * (_indexed_: would often be queried in a dashboard situation)

### types collection

* _id: ObjectId
* name: string
    * (_indexed_: would often be queried in a dashboard situation)
* weakAgainst: array of strings (denormalized type names)
* effectiveAgainst: array of strings (denormalized type names)

## Relationships

- Trainer
    - [x] trainers.pastOpponents: one-to-many and reflexive
        * => referencing
    - [x] trainers.pokemongs: one-to-many
        * => referencing + denormalizing on "nickname" and "species"
- Pokemong
    - [x] pokemongs.trainer: many-to-one
        * => referencing
    - [x] pokemongs.types: one-to-few [1;2]
        * => embedding
    - [x] pokemongs.moveSet: one-to-few [1;4] but will also need to be queried independently
        * => referencing + denormalizing on  "name"
- Move
    - [x] moves.type: one-to-one [1;1]
        * => embedding
- Type
    - [x] types.weakAgainst & types.effectiveAgainst: one-to-few, but reflexive
        * => denormalizing on "name"

## Cascades

- Pokemong
    - [x] delete ~> trainer.pokemongs
    - [x] update ~> trainer.pokemongs (denormalizing on "nickname" and "species")
    - [x] create ~> trainer.pokemongs
- Trainer
    - [x] delete ~> pokemong.trainer
    - [x] create ~> pokemong.trainer
- Move
    - [x] delete ~> pokemong.moveSet
    - [x] update ~> pokemong.moveSet (denormalizing on "name")
