class Survivor < ApplicationRecord
  belongs_to :location

  def as_json(options = {})
    super(include: :location)
  end
end
