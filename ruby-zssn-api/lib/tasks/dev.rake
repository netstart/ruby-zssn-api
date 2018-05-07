namespace :dev do
  desc "Popula dados para teste"
  task setup: :environment do

    puts "Cadastrando Recursos..."
    Resource.create!(
        name: "Water",
        point: 4,
        )

    Resource.create!(
        name: "Food",
        point: 3,
        )

    Resource.create!(
        name: "Medication",
        point: 2,
        )

    Resource.create!(
        name: "Ammunition",
        point: 1,
        )

    puts "Recursos cadastrados"

    puts "Cadastrando survivor..."
    100.times do |i|
      Survivor.create!(
          name: Faker::Name.name,
          age: Faker::Number.between(21, 60),
          gender: [:FEMALE, :MALE].sample,
          infected: [true, false].sample,
          location: Location.create!(
              latitude: Faker::Address.latitude,
              longitude: Faker::Address.longitude
          )
      )
    end
    puts "Survivor cadastrado"

  end

end
